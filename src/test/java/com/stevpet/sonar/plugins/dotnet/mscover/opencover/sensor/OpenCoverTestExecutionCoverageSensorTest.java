package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensorBehaviour;

public class OpenCoverTestExecutionCoverageSensorTest {

    public OpenCoverTestExecutionCoverageSensorBehavior bd = new OpenCoverTestExecutionCoverageSensorBehavior();
    private VsTestEnvironmentMock testEnvironment;
    private MsCoverPropertiesMock properties = new MsCoverPropertiesMock();
    private ProjectMock project = new ProjectMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironment = new MicrosoftWindowsEnvironmentMock();
    private OpenCoverCommandMock openCoverCommand = new OpenCoverCommandMock();
    
    @Before
    public void before() {
        testEnvironment = bd.getVsTestEnvironmentMock();
        bd.setMicrosoftWindowsEnvironment(microsoftWindowsEnvironment.getMock());
        bd.setProject(project.getMock());
        bd.setMsCoverProperties(properties.getMock());
       
    }
    @Test
    public void testSensorCreation() {
       bd.givenANewSensor();
       bd.verifyThatSensorExists();
        }
    

    @Test
    public void testShouldExecuteOnProjectProject_TestsAlreadyExecuted_HasNoProject_IsCSharp_RunOpenCover_ShouldNotExecute() {
        bd.givenANewSensor();
        microsoftWindowsEnvironment.givenTestsHaveExecuted(true);
        microsoftWindowsEnvironment.givenHasTestProject();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        properties.givenRunOpenCover(true);

        bd.verifyShouldAnalyseReturns(false);
    }

    @Test
    public void testShouldExecuteOnProjectProject_TestsNotExecuted_HasTestProject_IsNotCSharp_RunOpenCoverShouldNotExecute() {
        bd.givenANewSensor();
        microsoftWindowsEnvironment.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironment.givenHasTestProject();
        microsoftWindowsEnvironment.givenTestsHaveExecuted(false);
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(false);
        properties.givenRunOpenCover(true);

        bd.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecuted_SolutionHasNoTestProjects_ProjectIsCsharp__ShouldNotExecute() {
        bd.givenANewSensor();
        microsoftWindowsEnvironment.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironment.givenHasNoTestProjects();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        properties.givenRunOpenCover(true);
        bd.verifyShouldAnalyseReturns(false);
    }
    
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_DoNotRunOpenCover_ShouldExecute() {
        bd.givenANewSensor();
        microsoftWindowsEnvironment.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironment.givenHasTestProject();
        project.givenIsRootProject(false);
        project.givenIsCSharpProject(true);
        properties.givenRunOpenCover(false);
        bd.verifyShouldAnalyseReturns(false);
    }
    @Test
    public void testShouldExecuteOnProject_TestsNotExecutedSolutionHasTestProject_ProjectIsCsharp_RunOpenCover_ShouldExecute() {
        bd.givenANewSensor();
        microsoftWindowsEnvironment.givenTestsHaveExecuted(false);
        microsoftWindowsEnvironment.givenHasTestProject();
        project.givenIsCSharpProject(true);
        project.givenIsRootProject(false);
        properties.givenRunOpenCover(true);
        bd.verifyShouldAnalyseReturns(true);
    }
    
    @Test
    public void analyseSimpleSolution() {
        bd.givenANewSensor();
        bd.setOpenCoverCommand(openCoverCommand.getMock());
        microsoftWindowsEnvironment.givenHasSolutionWithProject(1);
        bd.analyse();
        //testEnvironment.verifyTestsHaveRun();
        //TODO: fix unit test
    }


}
