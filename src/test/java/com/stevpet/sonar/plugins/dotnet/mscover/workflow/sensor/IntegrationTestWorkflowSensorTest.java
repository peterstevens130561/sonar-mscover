package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;



import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;


import com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.VsTestIntegrationTestWorkflowSteps;


public class IntegrationTestWorkflowSensorTest extends WorkflowSensorTestUtil {
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
       
        sensor = new IntegrationTestWorkflowSensor(vsTestEnvironment, propertiesHelper, fileSystem, microsoftWindowsEnvironment, pathResolver,workflowDirector);
    }
    
    @Test
    public void ExecutedOnce() {
        when(propertiesHelper.isIntegrationTestsEnabled()).thenReturn(true);
        whenAnalysed();
        thenExecutedOnce();
    }
    
    @Test
    public void VsTest() {
        when(propertiesHelper.isIntegrationTestsEnabled()).thenReturn(true);
        whenAnalysed();
        assertEquals("expect vsTest based integration test workflow",VsTestIntegrationTestWorkflowSteps.class,workflowDirector.getWorkflow().getClass());
    }
}
