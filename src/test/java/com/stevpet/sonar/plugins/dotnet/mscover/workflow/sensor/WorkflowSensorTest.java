package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.picocontainer.DefaultPicoContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.VsTestIntegrationTestWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;
public class WorkflowSensorTest {

    WorkflowSensor sensor ;
    @Mock private VsTestEnvironment vsTestEnvironment;
    @Mock private MsCoverProperties propertiesHelper;
    @Mock private FileSystem fileSystem;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private PathResolver pathResolver;
    private WorkFlowSpy workflowDirector = new WorkFlowSpy();
    @Mock private Project project;
    @Mock private SensorContext context;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        sensor = new UnitTestWorkflowSensor(vsTestEnvironment, propertiesHelper, fileSystem, microsoftWindowsEnvironment, pathResolver,workflowDirector);
    }
    
    @Test
    public void NotRoot_NotExecute() {
        when(project.isRoot()).thenReturn(false);
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
    public void RootProject_RunmodeReUse_Execute() {
        when(project.isRoot()).thenReturn(true);
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.REUSE);
        boolean result=sensor.shouldExecuteOnProject(project);        
        assertTrue("should execute on project that is  root, and runmode reuse",result);
    }
    
    @Test
    public void RootProject_RunmodeVsTest_Execute() {
        when(project.isRoot()).thenReturn(true);
        when(propertiesHelper.getRunMode()).thenReturn(RunMode.RUNVSTEST);
        boolean result=sensor.shouldExecuteOnProject(project);        
        assertTrue("should execute on project that is  root, and runmode runvstest",result);
    }
    
    @Test
    public void Analyse_RunUnitTestOpenCover_ShouldSelectOpenCover() {
        sensor.analyse(project, context);
        assertEquals("Expect workflow to be of type",workflowDirector.getWorkflow(),VsTestIntegrationTestWorkflowSteps.class);
    }
    
    private class WorkFlowSpy implements WorkflowDirector {
        
        private WorkflowSteps workflow;

        
        public WorkflowSteps getWorkflow() {
            return workflow;
        }
        @Override
        public void wire(DefaultPicoContainer container) {
            workflow=container.getComponent(WorkflowSteps.class);
        }

        @Override
        public void execute() {
            // TODO Auto-generated method stub
            
        }
        
        
    }
}
