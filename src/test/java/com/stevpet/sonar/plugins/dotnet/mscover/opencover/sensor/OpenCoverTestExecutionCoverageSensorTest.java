package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.OpenCoverCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.ProjectSeamMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.ModuleFileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensorBehaviour;

public class OpenCoverTestExecutionCoverageSensorTest {

    public OpenCoverTestExecutionCoverageSensorBehavior classUnderTest = new OpenCoverTestExecutionCoverageSensorBehavior();
    private VsTestEnvironmentMock testEnvironmentMock;
    private MsCoverPropertiesMock properties = new MsCoverPropertiesMock();
    private ProjectMock project = new ProjectMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private OpenCoverCommandMock openCoverCommand = new OpenCoverCommandMock();
    private ProjectSeamMock projectSeamMock = new ProjectSeamMock();
    private ModuleFileSystemMock moduleFileSystemMock = new ModuleFileSystemMock();
    private OpenCoverCommandBuilderMock openCoverCommandBuilderMock = new OpenCoverCommandBuilderMock();
    private AssembliesFinderFactoryMock assembliesFinderFactoryMock = new AssembliesFinderFactoryMock();
    private AssembliesFinderMock assembliesFinderMock = new AssembliesFinderMock();
    private CommandLineExecutorMock commandLineExecutorMock = new CommandLineExecutorMock();
    private VSTestStdOutParserMock vsTestStdOutParserMock = new VSTestStdOutParserMock();
    private OpenCoverParserFactoryMock openCoverParserFactoryMock = new OpenCoverParserFactoryMock();
    private XmlParserSubjectMock xmlParserSubjectMock = new XmlParserSubjectMock();
    
    @Before
    public void before() {
        testEnvironmentMock = classUnderTest.getVsTestEnvironmentMock();
        classUnderTest.setMicrosoftWindowsEnvironment(microsoftWindowsEnvironmentMock.getMock());
        classUnderTest.setProject(project.getMock());
        classUnderTest.setMsCoverProperties(properties.getMock());
       
    }
    @Test
    public void testSensorCreation() {
       classUnderTest.givenANewSensor();
       classUnderTest.verifyThatSensorExists();
        }
    

    @Test
    public void testShouldExecuteOnProjectProject_TestsAlreadyExecuted_HasNoProject_IsCSharp_RunOpenCover_ShouldNotExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(true);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        properties.givenRunOpenCover(true);

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
        properties.givenRunOpenCover(true);

        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecuted_SolutionHasNoTestProjects_ProjectIsCsharp__ShouldNotExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasNoTestProjects();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        properties.givenRunOpenCover(true);
        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_DoNotRunOpenCover_ShouldExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        properties.givenRunOpenCover(false);
        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_RunOpenCover_ShouldExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsCSharpProject(true);
        project.givenIsRootProject(false);
        properties.givenRunOpenCover(true);
        classUnderTest.verifyShouldAnalyseReturns(true);
    }
    
    @Test
    public void analyseSimpleSolution() {
        moduleFileSystemMock.givenWorkingDir("OpenCoverTestExecutionCoverageSensorTest/.sonar");
        testEnvironmentMock.givenXmlCoveragePath("coverage.xml");
        classUnderTest.setModuleFileSystem(moduleFileSystemMock);
        classUnderTest.givenANewSensor();
        classUnderTest.givenOpenCoverCommandBuilder(openCoverCommandBuilderMock);
        classUnderTest.givenCommandLineExecutor(commandLineExecutorMock);
        classUnderTest.givenVsTestStdOutParser(vsTestStdOutParserMock);
        openCoverParserFactoryMock.givenXmlParserSubject(xmlParserSubjectMock);
        classUnderTest.givenOpenCoverParserFactory(openCoverParserFactoryMock);
        assembliesFinderMock.onFindUnitTestAssembliesDir("OpenCoverTestExecutionCoverageSensorTest/assemblies");
        
        assembliesFinderFactoryMock.onCreate(properties,assembliesFinderMock);
        classUnderTest.givenAssembliesFinderFactory(assembliesFinderFactoryMock);
        classUnderTest.givenProjectSeam(projectSeamMock);
        classUnderTest.setOpenCoverCommand(openCoverCommand.getMock());
        microsoftWindowsEnvironmentMock.givenHasSolutionWithProject(1);
        classUnderTest.analyse();
        //testEnvironment.verifyTestsHaveRun();
        //TODO: fix unit test
    }


}
