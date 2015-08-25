package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class WorkflowSensorTest extends WorkflowSensorTestUtil {
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        sensor = new UnitTestWorkflowSensor(vsTestEnvironment, propertiesHelper, fileSystem, microsoftWindowsEnvironment, pathResolver,workflowDirector);
    }
    
    @Test
    public void Root_NotExecute() {
        when(project.isRoot()).thenReturn(true);
        boolean result=sensor.shouldExecuteOnProject(project);
        
        assertFalse("should not execute on project that is not root",result);
    }
    
    @Test
    public void RootProject_RunmodeSkip_NotExecute() {
        when(project.isRoot()).thenReturn(true);
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.SKIP);
        boolean result=sensor.shouldExecuteOnProject(project);        
        assertFalse("should not execute on project that is  root, and runmode skip",result);
    }
    
    @Test
    public void ChildProject_RunmodeReUse_Execute() {
        when(project.isRoot()).thenReturn(false);
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.REUSE);
        when(microsoftWindowsEnvironment.hasUnitTestSourceFiles()).thenReturn(true);
        boolean result=sensor.shouldExecuteOnProject(project);        
        assertTrue("should execute on project that is  child, and runmode reuse",result);
    }
    
    @Test
    public void ChildProject_RunmodeVsTest_Execute() {
        when(project.isRoot()).thenReturn(false);
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.RUNVSTEST);
        when(microsoftWindowsEnvironment.hasUnitTestSourceFiles()).thenReturn(true);
        
        boolean result=sensor.shouldExecuteOnProject(project);     

        assertTrue("should execute on project that is  child, and runmode runvstest",result);
    }
    
}
