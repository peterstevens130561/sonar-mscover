package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import static org.junit.Assert.assertEquals;

import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class WorkflowSensorTestUtil {

    protected WorkflowSensor sensor;
    @Mock
    protected VsTestEnvironment vsTestEnvironment;
    @Mock
    protected MsCoverConfiguration propertiesHelper;
    @Mock
    protected FileSystem fileSystem;
    @Mock
    protected MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock
    protected PathResolver pathResolver;
    protected WorkFlowSpy workflowDirector = new WorkFlowSpy();
    @Mock
    protected Project project;
    @Mock
    protected SensorContext context;

    public WorkflowSensorTestUtil() {
        super();
    }
    
    class WorkFlowSpy implements WorkflowDirector {
        
        private WorkflowSteps workflow;
        private int executedCount ;
        
        public WorkflowSteps getWorkflow() {
            return workflow;
        }
        
        public int getExecutedCount() {
            return executedCount;
        }
        @Override
        public void wire(DefaultPicoContainer container) {
            workflow=container.getComponent(WorkflowSteps.class);
        }

        @Override
        public void execute() {
            executedCount++;
        }

    }

    protected void whenAnalysed() {
        sensor.analyse(project, context);
    }

    protected void thenExecutedOnce() {
        assertEquals("expect that plugin is exeucted once",1,workflowDirector.getExecutedCount());
    }

}