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
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
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

    private FileSystem fileSystem;

    private Settings settings;

    private InspectCodeResultsParser inspectCodeResultsParser;
    private InspectCodeIssuesSaver inspectCodeIssuesSaver;

    private InspectCodeRunner inspectCodeRunner;

    /**
     * Constructs a {@link org.sonar.plugins.csharp.resharper.ReSharperSensor}.
     */
    public ReSharperSensor(FileSystem fileSystem,
            Settings settings,
            InspectCodeResultsParser inspectCodeResultsParser,
            InspectCodeIssuesSaver inspectCodeIssuesSaver,
            InspectCodeRunner inspectCodeRunner) {
        this.fileSystem = fileSystem;
        this.settings = settings;
        this.inspectCodeResultsParser = inspectCodeResultsParser;
        this.inspectCodeIssuesSaver= inspectCodeIssuesSaver;
        this.inspectCodeRunner = inspectCodeRunner;

    }

    /**
     * {@inheritDoc}
     */
    public void analyse(Project project, SensorContext context) {

        Collection<File> reportFiles = null;
        reportFiles = inspectCodeRunner.inspectCode(project);
        for (File reportFile : reportFiles) {
            LOG.debug("Analysing report" + reportFile.getName());
            List<InspectCodeIssue>issues=inspectCodeResultsParser.parse(reportFile);
            inspectCodeIssuesSaver.saveIssues(issues);
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        boolean hasCs = fileSystem.languages().contains("cs");
        boolean skip = ReSharperConstants.MODE_SKIP.equalsIgnoreCase(settings.getString(ReSharperConstants.MODE));
        boolean isRoot = project.isRoot();
        return hasCs && !skip && isRoot;
    }

}
