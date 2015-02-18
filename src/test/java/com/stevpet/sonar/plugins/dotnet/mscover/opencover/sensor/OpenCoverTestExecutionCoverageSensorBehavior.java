/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.OpenCoverCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.ProjectSeamMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverTestExecutionCoverageSensorBehavior {
    OpenCoverTestExecutionCoverageSensor sensor;
    private MsCoverProperties msCoverProperties;
    private VsTestEnvironmentMock vsTestEnvironment = new VsTestEnvironmentMock();
    private FileSystem fileSystem = mock(FileSystem.class);
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
    private Project project = mock(Project.class);
    private SensorContext context = mock(SensorContext.class);

    
    public void setModuleFileSystem(FileSystemMock fileSystemMock ) {
        fileSystem = fileSystemMock.getMock();
    }

    public void givenANewSensor() {
        sensor=new OpenCoverTestExecutionCoverageSensor(msCoverProperties, 
                microsoftWindowsEnvironment, 
                fileSystem, 
                vsTestEnvironment.getMock());
    }

    public void verifyThatSensorExists() {
        assertNotNull(sensor);
    }
    
    public VsTestEnvironmentMock getVsTestEnvironmentMock() {
        return vsTestEnvironment;
    }

    public void verifyShouldAnalyseReturns(boolean expected) {
        boolean result=sensor.shouldExecuteOnProject(project);
        assertEquals(expected,result);
    }

    public void setProject(Project project) {
        this.project=project;
    }

    public void setMsCoverProperties(MsCoverProperties msCoverProperties) {
        this.msCoverProperties=msCoverProperties;
    }

    public void setMicrosoftWindowsEnvironment(MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
    }

    public void setOpenCoverCommand(OpenCoverCommand mock) {
        sensor.setOpenCoverCommand(mock);
    }

    public void analyse() {
        sensor.analyse(project, context);      
    }

    public void givenProjectSeam(ProjectSeamMock projectSeamMock) {
        sensor.setProjectSeam(projectSeamMock.getMock());
    }

    public void givenOpenCoverCommandBuilder(
            OpenCoverCommandBuilderMock openCoverCommandBuilderMock) {
        sensor.setOpenCoverCommandBuilder(openCoverCommandBuilderMock.getMock());
    }

    public void givenAssembliesFinderFactory(
            AssembliesFinderFactoryMock assembliesFinderFactoryMock) {
        sensor.setAssembliesFinderFactory(assembliesFinderFactoryMock.getMock());
    }

    public void givenCommandLineExecutor(
            CommandLineExecutorMock commandLineExecutorMock) {
        sensor.setCommandLineExecutor(commandLineExecutorMock.getMock());
    }

    public void givenVsTestStdOutParser(VSTestStdOutParserMock vsTestStdOutParserMock) {
        sensor.setVsTestStdOutParser(vsTestStdOutParserMock.getMock());
    }

    public void givenOpenCoverParserFactory(
            OpenCoverParserFactoryMock openCoverParserFactoryMock) {
        sensor.setOpenCoverParserFactory(openCoverParserFactoryMock.getMock());
    }

    public void givenTestRunnerFactory(
            VsTestRunnerFactoryMock vsTestRunnerFactoryMock) {
        sensor.setVsTestRunnerFactory(vsTestRunnerFactoryMock.getMock());
    }

    public void givenFakesRemover(FakesRemoverMock fakesRemoverMock) {
        sensor.setFakesRemover(fakesRemoverMock.getMock());
    }

    public void setFileSystem(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

}
