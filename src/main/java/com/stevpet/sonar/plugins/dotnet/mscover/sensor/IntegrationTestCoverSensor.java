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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.DefaultResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
@Extension
public class IntegrationTestCoverSensor implements Sensor {

    static final Logger LOG = LoggerFactory
            .getLogger(IntegrationTestCoverSensor.class);

    private final MsCoverProperties propertiesHelper ;
    private TimeMachine timeMachine;
    private CoverageSaver coverageHelper;
    private AbstractCoverageHelperFactory coverageHelperFactory ;
    private ResourceMediatorFactory resourceMediatorFactory = new DefaultResourceMediatorFactory();
    private CommandLineExecutor executor = new WindowsCommandLineExecutor();

    private ShouldExecuteHelper shouldExecuteHelper;

    private FileSystem fileSystem;

    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    /**
     * Use of IoC to get Settings
     */
    public IntegrationTestCoverSensor(MsCoverProperties propertiesHelper,
            TimeMachine timeMachine,
            AbstractCoverageHelperFactory coverageHelperFactory,
            FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.propertiesHelper = propertiesHelper;
        this.timeMachine=timeMachine;
        this.coverageHelperFactory = coverageHelperFactory;
        this.fileSystem=fileSystem;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        shouldExecuteHelper = coverageHelperFactory.createShouldExecuteHelper(propertiesHelper);
    }
    

    public boolean shouldExecuteOnProject(Project project) {
        return shouldExecuteHelper.shouldExecuteOnProject(project) && propertiesHelper.isIntegrationTestsEnabled() ;
    }

    public void analyse(Project project, SensorContext sensorContext) {
        LOG.info("Running IntegrationTestCoverSensor");
        ResourceMediator resourceMediator = resourceMediatorFactory
                .createWithFilters(sensorContext, project, timeMachine,
                        propertiesHelper, fileSystem);
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext,
                resourceMediator);
        coverageHelper = coverageHelperFactory
                .createIntegrationTestCoverageHelper(fileSystem, measureSaver);
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
        List<String> modules = microsoftWindowsEnvironment.getCurrentSolution().getModules();
        coverageHelper.analyse(project, xmlPath,modules);
    }


    private boolean transformationNeeded(String xmlPath,String coveragePath) {
        File xmlFile = new File(xmlPath);
        File coverageFile = new File(coveragePath);
        return !xmlFile.exists() || FileUtils.isFileNewer(coverageFile, xmlFile);

    }

}
