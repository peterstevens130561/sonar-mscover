package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.*;

import org.junit.Test;


public class VsTestExecutionSensorTest  {
    private VsTestExecutionSensorBehaviour bd = new VsTestExecutionSensorBehaviour();
    @Test
    public void testVsTestExecutionSensor() {
        bd.givenANewTestExecutionSensor();
        bd.verifyThatTheTestExecutionSensorExists();
    }
    
    @Test
    public void runAnalysisForEmptyProject() {
        bd.givenANewTestExecutionSensor();
        bd.givenTestsHaveExecuted();
        bd.givenAnalysedProjectIsCSharpProject();
        bd.givenStubbedVsTestRunner();
     
        bd.analyseProject();
        
    }

    @Test
    public void testShouldExecuteOnProject() {
        fail("Not yet implemented");
    }

    @Test
    public void testAnalyse() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetVsTestRunnerFactory() {
        fail("Not yet implemented");
    }

}
