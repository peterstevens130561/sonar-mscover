/*
 * Sonar .NET Plugin :: ReSharper
 * Copyright (C) 2013 John M. Wright
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugings.dotnet.resharper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugings.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugings.dotnet.resharper.inspectcode.ReSharperRunner;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Collects the ReSharper reporting into sonar.
 */
public class ReSharperSensor implements Sensor {

    private static final Logger LOG = LoggerFactory
            .getLogger(ReSharperSensor.class);

    private ProjectFileSystem fileSystem;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    private Settings settings;

    private InspectCodeResultsParser inspectCodeResultsParser;

    /**
     * Constructs a {@link org.sonar.plugins.csharp.resharper.ReSharperSensor}.
     */
    public ReSharperSensor(ProjectFileSystem fileSystem,
            Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,InspectCodeResultsParser inspectCodeResultsParser) {
        this.fileSystem = fileSystem;
        this.settings = settings;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.inspectCodeResultsParser = inspectCodeResultsParser;

    }

    /**
     * {@inheritDoc}
     */
    public void analyse(Project project, SensorContext context) {

        Collection<File> reportFiles = null;
        String mode = settings.getString(ReSharperConstants.MODE);
        if (ReSharperConstants.MODE_REUSE_REPORT.equalsIgnoreCase(mode)) {
            // reportFiles = getExistingReports(project);
        } else {
            reportFiles = inspectCode(project);
        }
        if (reportFiles == null || reportFiles.isEmpty()) {
            LOG.warn("Nothing to report");
            return;
        }
        // and analyze results
        for (File reportFile : reportFiles) {
            LOG.debug("Analysing report" + reportFile.getName());
            analyseResults(reportFile);
        }
    }

    private Collection<File> inspectCode(Project project) {
        File reportFile;
        try {
            ReSharperRunner runner = ReSharperRunner.create(settings
                    .getString(ReSharperConstants.INSTALL_DIR_KEY));
            VisualStudioSolution vsSolution = microsoftWindowsEnvironment
                    .getCurrentSolution();
            List<String> properties = getProperties();
            ReSharperCommandBuilder builder = runner.createCommandBuilder(
                    vsSolution, properties);
            reportFile = new File(fileSystem.getSonarWorkingDirectory(),
                    ReSharperConstants.REPORT_FILENAME);
            builder.setReportFile(reportFile);

            String additionalArguments = settings
                    .getString(ReSharperConstants.INSPECTCODE_PROPERTIES);
            builder.setProperties(additionalArguments);
            String cachesHome = settings
                    .getString(ReSharperConstants.CACHES_HOME);
            builder.setCachesHome(cachesHome);

            String profile = settings
                    .getString(ReSharperConstants.INSPECTCODE_PROFILE);
            builder.setProfile(profile);

            int timeout = settings
                    .getInt(ReSharperConstants.TIMEOUT_MINUTES_KEY);
            if(timeout==0) {
                timeout=60;
            }
            runner.execute(builder, timeout);
        } catch (ReSharperException e) {
            throw new SonarException("ReSharper execution failed."
                    + e.getMessage(), e);
        }
        Collection<File> reportFiles = Collections.singleton(reportFile);
        return reportFiles;
    }

    private List<String> getProperties() {
        List<String> properties = new ArrayList<String>();
        addPropertyIfDefined(properties, "Platform",
                "sonar.dotnet.buildPlatform");
        addPropertyIfDefined(properties, "Configuration",
                "sonar.dotnet.buildConfiguration");
        return properties;
    }

    private void addPropertyIfDefined(List<String> properties,
            String msBuildPropertyName, String sonarPropertyName) {
        String value = settings.getString(sonarPropertyName);
        if (!StringUtils.isEmpty(value)) {
            value = value.replace(" ", "");
            properties.add(msBuildPropertyName + "=" + value);
        }
    }

    private void analyseResults(File reportFile) throws SonarException {
        if (reportFile.exists()) {
            LOG.debug("ReSharper report found at location" + reportFile);
            List<InspectCodeIssue>issues=inspectCodeResultsParser.parse(reportFile);
            
        } else {
            String msg = "No ReSharper report found for path " + reportFile;
            LOG.error(msg);
            throw new SonarException(msg);
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

}
