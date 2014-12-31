/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.OpenCoverCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.ProjectSeamMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.ModuleFileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverTestExecutionCoverageSensorTest {

    public OpenCoverTestExecutionCoverageSensorBehavior classUnderTest = new OpenCoverTestExecutionCoverageSensorBehavior();
    private VsTestEnvironmentMock testEnvironmentMock;
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private ProjectMock project = new ProjectMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private OpenCoverCommandMock openCoverCommandMock = new OpenCoverCommandMock();
    private ProjectSeamMock projectSeamMock = new ProjectSeamMock();
    private ModuleFileSystemMock moduleFileSystemMock = new ModuleFileSystemMock();
    private OpenCoverCommandBuilderMock openCoverCommandBuilderMock = new OpenCoverCommandBuilderMock();
    private AssembliesFinderFactoryMock assembliesFinderFactoryMock = new AssembliesFinderFactoryMock();
    private AssembliesFinderMock assembliesFinderMock = new AssembliesFinderMock();
    private CommandLineExecutorMock commandLineExecutorMock = new CommandLineExecutorMock();
    private VSTestStdOutParserMock vsTestStdOutParserMock = new VSTestStdOutParserMock();
    private OpenCoverParserFactoryMock openCoverParserFactoryMock = new OpenCoverParserFactoryMock();
    private XmlParserSubjectMock xmlParserSubjectMock = new XmlParserSubjectMock();
    private VsTestRunnerFactoryMock vsTestRunnerFactoryMock = new VsTestRunnerFactoryMock();
    private VsTestRunnerMock vsTestRunnerMock = new VsTestRunnerMock();
    private FakesRemoverMock fakesRemoverMock = new FakesRemoverMock();
    
    @Before
    public void before() {
        testEnvironmentMock = classUnderTest.getVsTestEnvironmentMock();
        classUnderTest.setMicrosoftWindowsEnvironment(microsoftWindowsEnvironmentMock.getMock());
        classUnderTest.setProject(project.getMock());
        classUnderTest.setMsCoverProperties(msCoverPropertiesMock.getMock());
       
    }
    @Test
    public void testSensorCreation() {
       classUnderTest.givenANewSensor();
       classUnderTest.verifyThatSensorExists();
        }
    

    @Test
    public void testShouldExecuteOnProjectProject_TestsAlreadyExecuted_HasNoProject_IsCSharp_RunOpenCover_ShouldNotExecute() {
        classUnderTest.givenANewSensor();
        // make sure it is not used anymore!
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        testEnvironmentMock.givenTestsHaveExecuted();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        msCoverPropertiesMock.givenRunOpenCover(true);

        classUnderTest.verifyShouldAnalyseReturns(false);
    }

    @Test
    public void testShouldExecuteOnProjectProject_TestsNotExecuted_HasTestProject_IsNotCSharp_RunOpenCoverShouldNotExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(false);
        msCoverPropertiesMock.givenRunOpenCover(true);

        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecuted_SolutionHasNoTestProjects_ProjectIsCsharp__ShouldNotExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasNoTestProjects();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        msCoverPropertiesMock.givenRunOpenCover(true);
        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_DoNotRunOpenCover_ShouldExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        msCoverPropertiesMock.givenRunOpenCover(false);
        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_RunOpenCover_ShouldExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsCSharpProject(true);
        project.givenIsRootProject(false);
        msCoverPropertiesMock.givenRunOpenCover(true);
        classUnderTest.verifyShouldAnalyseReturns(true);
    }
    
    @Test
    public void analyseSimpleSolution() {
        String targetDir="OpenCoverTestExecutionCoverageSensorTest/assemblies";
        moduleFileSystemMock.givenWorkingDir("OpenCoverTestExecutionCoverageSensorTest/.sonar");
        testEnvironmentMock.givenXmlCoveragePath("coverage.xml");
        classUnderTest.setModuleFileSystem(moduleFileSystemMock);
        classUnderTest.givenANewSensor();
        classUnderTest.givenOpenCoverCommandBuilder(openCoverCommandBuilderMock);
        classUnderTest.givenCommandLineExecutor(commandLineExecutorMock);
        classUnderTest.givenVsTestStdOutParser(vsTestStdOutParserMock);
        classUnderTest.givenFakesRemover(fakesRemoverMock);
        
        classUnderTest.givenTestRunnerFactory(vsTestRunnerFactoryMock);
        vsTestRunnerFactoryMock.onCreate(vsTestRunnerMock);
        
        openCoverParserFactoryMock.givenXmlParserSubject(xmlParserSubjectMock);
        classUnderTest.givenOpenCoverParserFactory(openCoverParserFactoryMock);
        assembliesFinderMock.onFindUnitTestAssembliesDir(targetDir);
        
        assembliesFinderFactoryMock.onCreate(msCoverPropertiesMock,assembliesFinderMock);
        classUnderTest.givenAssembliesFinderFactory(assembliesFinderFactoryMock);
        classUnderTest.givenProjectSeam(projectSeamMock);
        classUnderTest.setOpenCoverCommand(openCoverCommandMock.getMock());
        microsoftWindowsEnvironmentMock.givenHasSolutionWithProject(1);
        classUnderTest.analyse();
        
        
        xmlParserSubjectMock.verifyParseFile("coverage.xml");
        openCoverCommandMock.verifySetTargetDir(targetDir);
        openCoverCommandBuilderMock.verifySetOpenCovercommand(openCoverCommandMock);
        openCoverCommandBuilderMock.verifySetSolution();
        openCoverCommandBuilderMock.verifySetMsCoverProperties(msCoverPropertiesMock);
        openCoverCommandBuilderMock.verifySetTestRunner(vsTestRunnerMock);
        
        testEnvironmentMock.verifyTestsHaveRun();
        testEnvironmentMock.verifySonarCoverageSet();
        
        fakesRemoverMock.verifyRemoveFakes(new File(targetDir));
    }


}
