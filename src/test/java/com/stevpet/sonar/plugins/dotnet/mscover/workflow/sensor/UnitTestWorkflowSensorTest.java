package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.OpenCoverWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.VsTestWorkflowSteps;
public class UnitTestWorkflowSensorTest extends WorkflowSensorTestUtil {

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        sensor = new UnitTestWorkflowSensor(vsTestEnvironment, propertiesHelper, fileSystem, microsoftWindowsEnvironment, pathResolver,workflowDirector);
    }
    
    @Test
    public void NoPropertiesSpecified_NullWorkflow() {
        whenAnalysed();
        assertEquals("Properties not specified, so expect null workflow",NullWorkflowSteps.class,workflowDirector.getWorkflow().getClass());
    }
    
    @Test
    public void VsTestSpecified_VsTest() {
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.RUNVSTEST);
        when(propertiesHelper.runOpenCover()).thenReturn(false);
        whenAnalysed();
        assertEquals("runvstest specified, coveragetool not specified, so expect VsTest workflow",VsTestWorkflowSteps.class,workflowDirector.getWorkflow().getClass());
    }
    
    @Test
    public void OpenCoverSpecified_VsTest() {
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.RUNVSTEST);
        when(propertiesHelper.runOpenCover()).thenReturn(true);
        whenAnalysed();
        assertEquals("runvstest specified, coveragetool specified, so expect OpenCover workflow",OpenCoverWorkflowSteps.class,workflowDirector.getWorkflow().getClass());
    }
    
    @Test
    public void NothingSpecified_ExecutedOnce() {
        whenAnalysed();
        thenExecutedOnce();
    }
    
    @Test
    public void OpenCoverSpecified_ExecutedOnce() {
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.RUNVSTEST);
        when(propertiesHelper.runOpenCover()).thenReturn(true);
        whenAnalysed();
        thenExecutedOnce();
    }

    @Test
    public void OpenCoverNotSpecified_ExecutedOnce() {
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.RUNVSTEST);
        when(propertiesHelper.runOpenCover()).thenReturn(false);
        whenAnalysed();
        thenExecutedOnce();
    }

}
