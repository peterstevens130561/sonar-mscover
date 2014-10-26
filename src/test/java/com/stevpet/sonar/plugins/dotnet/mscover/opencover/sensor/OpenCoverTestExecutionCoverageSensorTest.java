package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestExecutionSensorBehaviour;

public class OpenCoverTestExecutionCoverageSensorTest {

    public OpenCoverTestExecutionCoverageSensorBehavior bd = new OpenCoverTestExecutionCoverageSensorBehavior();
    private VsTestEnvironmentMock testEnvironment;
    
    @Before
    public void before() {
        testEnvironment = bd.getVsTestEnvironmentMock();
    }
    @Test
    public void testVsTestExecutionSensor() {
       bd.givenANewSensor();
       bd.verifyThatSensorExists();
        }
    
    @Test
    public void testGetSupportedLanguages() {
        fail("Not yet implemented");
    }

    @Test
    public void testShouldExecuteOnProjectProject_AlreadyExecuted_ShouldNotExecute() {
        bd.givenANewSensor();
        testEnvironment.givenTestsHaveExecuted();
        bd.verifyShouldAnalyseReturns(false);
        
        fail("Not yet implemented");
    }

    @Test
    public void testAnalyseProjectSensorContext() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetResultPaths() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetAssembliesToIncludeInCoverageFilter() {
        fail("Not yet implemented");
    }

    @Test
    public void testListCoveredAssemblies() {
        fail("Not yet implemented");
    }

}
