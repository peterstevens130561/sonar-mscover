/*
 * Sonar .NET Plugin :: MsCover
 * Copyright (C) 2014 Peter Stevens
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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
@Extension
public class IntegrationTestCoverSensor implements Sensor {

    static final Logger LOG = LoggerFactory
            .getLogger(IntegrationTestCoverSensor.class);

    private final MsCoverProperties propertiesHelper ;

    private AbstractCoverageHelperFactory coverageHelperFactory ;
    private CommandLineExecutor executor = new WindowsCommandLineExecutor();

    private ShouldExecuteHelper shouldExecuteHelper;

    private FileSystem fileSystem;

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    private MeasureSaver measureSaver;

    /**
     * Use of IoC to get Settings
     */
    public IntegrationTestCoverSensor(MsCoverProperties propertiesHelper,
            AbstractCoverageHelperFactory coverageHelperFactory,
            FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MeasureSaver measureSaver) {
        this.propertiesHelper = propertiesHelper;
        this.coverageHelperFactory = coverageHelperFactory;
        this.fileSystem=fileSystem;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.measureSaver = measureSaver;
        shouldExecuteHelper = coverageHelperFactory.createShouldExecuteHelper(propertiesHelper);
    }
    

    public boolean shouldExecuteOnProject(Project project) {
        return shouldExecuteHelper.shouldExecuteOnProject(project) && propertiesHelper.isIntegrationTestsEnabled() ;
    }

    public void analyse(Project project, SensorContext sensorContext) {
        LOG.info("Running IntegrationTestCoverSensor");
       measureSaver.setProjectAndContext(project,sensorContext);
       

        List<String> artifactNames = microsoftWindowsEnvironment.getArtifactNames();
        CoverageSaver coverageHelper = coverageHelperFactory
                .createVsTestIntegrationTestCoverageHelper(fileSystem, measureSaver);
        if(StringUtils.isNotEmpty(propertiesHelper.getIntegrationTestsDir())) {
            String xmlPath = getCoverageXmlPath();
            coverageHelper.analyse(project, xmlPath,artifactNames);
            return;
        } else {
            String xmlPath = getCoverageXmlPath();
            coverageHelper.analyse(project, xmlPath,artifactNames); 
            return;
        }
        
    }


    private String getCoverageXmlPath() {
        String coveragePath = propertiesHelper.getIntegrationTestsPath();
        String xmlPath;
        if (coveragePath.endsWith(".coverage")) {
            xmlPath = coveragePath.replace(".coverage", ".xml");
            if (transformationNeeded(xmlPath, coveragePath)) {
                CodeCoverageCommand command = new WindowsCodeCoverageCommand();
                command.setSonarPath(fileSystem.workDir().getAbsolutePath());
                command.setCoveragePath(coveragePath);
                command.setOutputPath(xmlPath);
                command.install();
                LOG.info("IntegrationCoverSensor: creating .xml file");
                int exitCode = executor.execute(command);
                if (exitCode != 0) {
                    throw new SonarException("failed");
                }
            } else {
                LOG.info("Reusing xml file, as it is newer than the .coverage file");
            }
        } else if (coveragePath.endsWith(".xml")) {
            xmlPath = coveragePath;
        } else {
            throw new SonarException("Invalid coverage format " + coveragePath);
        }
        return xmlPath;
    }


    private boolean transformationNeeded(String xmlPath,String coveragePath) {
        File xmlFile = new File(xmlPath);
        File coverageFile = new File(coveragePath);
        return !xmlFile.exists() || FileUtils.isFileNewer(coverageFile, xmlFile);

    }

}
