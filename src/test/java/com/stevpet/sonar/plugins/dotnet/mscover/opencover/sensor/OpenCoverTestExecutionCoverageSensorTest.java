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
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
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
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        testEnvironmentMock.givenTestsHaveNotExecuted();
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        project.givenIsRootProject(false);
        fileSystemMock.givenLanguage("java");
        msCoverPropertiesMock.givenRunOpenCover(true);

        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecuted_SolutionHasNoTestProjects_ProjectIsCsharp__ShouldNotExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasNoTestProjects();
        project.givenIsRootProject(false);
        fileSystemMock.givenLanguage("cs");
        msCoverPropertiesMock.givenRunOpenCover(true);
        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_DoNotRunOpenCover_ShouldExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        project.givenIsRootProject(false);
        fileSystemMock.givenLanguage("cs");
        msCoverPropertiesMock.givenRunOpenCover(false);
        classUnderTest.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_RunOpenCover_ShouldExecute() {
        classUnderTest.givenANewSensor();
        microsoftWindowsEnvironmentMock.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironmentMock.givenHasTestProject();
        fileSystemMock.givenLanguage("cs");
        project.givenIsRootProject(true);
        msCoverPropertiesMock.givenRunOpenCover(true);
        classUnderTest.verifyShouldAnalyseReturns(true);
    }
    
    @Test
    public void analyseSimpleSolution() {
        String targetDir="OpenCoverTestExecutionCoverageSensorTest/assemblies";
        moduleFileSystemMock.givenWorkDir("OpenCoverTestExecutionCoverageSensorTest/.sonar");
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
