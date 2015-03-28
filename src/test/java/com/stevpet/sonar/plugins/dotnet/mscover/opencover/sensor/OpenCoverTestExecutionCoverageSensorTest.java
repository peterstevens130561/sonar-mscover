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

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.injectors.MultiInjection;
import org.picocontainer.injectors.AnnotatedFieldInjection;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.OpenCoverCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.Wirer;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMissingPdbObserverIgnoringSpecifiedPdbs;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.BaseAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverTestExecutionCoverageSensorTest {

    public OpenCoverTestExecutionCoverageSensorBehavior classUnderTest = new OpenCoverTestExecutionCoverageSensorBehavior();
    private VsTestEnvironmentMock testEnvironmentMock;
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private ProjectMock project = new ProjectMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private OpenCoverCommandMock openCoverCommandMock = new OpenCoverCommandMock();
    private FileSystemMock moduleFileSystemMock = new FileSystemMock();
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
    private FileSystemMock fileSystemMock = new FileSystemMock();
    
    @Before
    public void before() {
        testEnvironmentMock = classUnderTest.getVsTestEnvironmentMock();
        classUnderTest.setMicrosoftWindowsEnvironment(microsoftWindowsEnvironmentMock.getMock());
        classUnderTest.setProject(project.getMock());
        classUnderTest.setMsCoverProperties(msCoverPropertiesMock.getMock());
        classUnderTest.setFileSystem(fileSystemMock.getMock());
       
    }
    @Test
    public void testSensorCreation() {
       classUnderTest.givenANewSensor();
       classUnderTest.verifyThatSensorExists();
        }
    

    @Test
    public void testShouldExecuteOnProjectProject_TestsAlreadyExecuted_HasNoProject_IsCSharp_RunOpenCover_ShouldNotExecute() {
        classUnderTest.givenANewSensor();
        testEnvironmentMock.givenTestsHaveExecuted();
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsRootProject(false);
        fileSystemMock.givenLanguage("cs");
        msCoverPropertiesMock.givenRunOpenCover(true);

        classUnderTest.verifyShouldAnalyseReturns(false);
    }

    @Test
    public void testShouldExecuteOnProjectProject_TestsNotExecuted_HasTestProject_IsNotCSharp_RunOpenCoverShouldNotExecute() {
        classUnderTest.givenANewSensor();
        testEnvironmentMock.givenTestsHaveNotExecuted();
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsRootProject(false);
        fileSystemMock.givenLanguage("java");
        msCoverPropertiesMock.givenRunOpenCover(true);

        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecuted_SolutionHasNoTestProjects_ProjectIsCsharp__ShouldNotExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenHasNoTestProjects();
        project.givenIsRootProject(false);
        fileSystemMock.givenLanguage("cs");
        msCoverPropertiesMock.givenRunOpenCover(true);
        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_DoNotRunOpenCover_ShouldExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsRootProject(false);
        fileSystemMock.givenLanguage("cs");
        msCoverPropertiesMock.givenRunOpenCover(false);
        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_RunOpenCover_ShouldExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        fileSystemMock.givenLanguage("cs");
        project.givenIsRootProject(true);
        msCoverPropertiesMock.givenRunOpenCover(true);
        classUnderTest.verifyShouldAnalyseReturns(true);
    }
    
    @Test
    public void analyseSimpleSolution() {
        //Given a simple solution
        String targetDir="OpenCoverTestExecutionCoverageSensorTest/assemblies";
        moduleFileSystemMock.givenWorkDir("OpenCoverTestExecutionCoverageSensorTest/.sonar");
        
        VsTestEnvironment vsTestEnvironment = new VsTestEnvironment();
        vsTestEnvironment.setCoverageXmlPath("coverage.xml");
        vsTestEnvironment.setTargetDir(targetDir);
        
        fileSystemMock.givenWorkDir(new File("OpenCoverTestExecutionCoverageSensorTest/.sonar"));
        //When I wire all components
        OpenCoverCoverageParser openCoverCoverageParser = Wirer.instantiateOpenCoverCoverageParser(vsTestEnvironment,msCoverPropertiesMock.getMock());
        microsoftWindowsEnvironmentMock.givenHasSolutionWithProject(1);
        
        new IncorporatedVsTestRunner(msCoverPropertiesMock.getMock(),microsoftWindowsEnvironmentMock.getMock(),fileSystemMock.getMock()).build();
        
        DefaultPicoContainer picoContainer = new DefaultPicoContainer(new ConstructorInjection());
        picoContainer.addComponent(VsTestEmbeddedInOpenCoverCommandBuilder.class)
            .addComponent(openCoverCommandMock.getMock())
            .addComponent(OpenCoverCommandBuilder.class)
            .addComponent(vsTestRunnerMock.getMock())
            .addComponent(commandLineExecutorMock.getMock())
            .addComponent(msCoverPropertiesMock.getMock())
            .addComponent(vsTestEnvironment)
            .addComponent(fileSystemMock.getMock())
            .addComponent(fakesRemoverMock.getMock())
            .addComponent(vsTestStdOutParserMock.getMock())
            .addComponent(openCoverCoverageParser)
            .addComponent(microsoftWindowsEnvironmentMock.getMock());
    

        
        assembliesFinderMock.onFindUnitTestAssembliesDir(targetDir);
        

        VsTestEmbeddedInOpenCoverCommandBuilder commandBuilder=picoContainer.getComponent(VsTestEmbeddedInOpenCoverCommandBuilder.class);
        commandBuilder.execute();
        
        openCoverCoverageParser.execute();
        
        xmlParserSubjectMock.verifyParseFile("coverage.xml");
        openCoverCommandMock.verifySetTargetDir(targetDir);
        openCoverCommandBuilderMock.verifySetOpenCovercommand(openCoverCommandMock);
        openCoverCommandBuilderMock.verifySetAssemblies();
        openCoverCommandBuilderMock.verifySetMsCoverProperties(msCoverPropertiesMock);
        openCoverCommandBuilderMock.verifySetTestRunner(vsTestRunnerMock);
        
        testEnvironmentMock.verifyTestsHaveRun();
        testEnvironmentMock.verifySonarCoverageSet();
        
        fakesRemoverMock.verifyRemoveFakes(new File(targetDir));
    }



}
