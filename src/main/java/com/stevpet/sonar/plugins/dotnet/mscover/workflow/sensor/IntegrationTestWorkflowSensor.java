package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import org.picocontainer.DefaultPicoContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.VsTestIntegrationTestWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;

public class IntegrationTestWorkflowSensor extends WorkflowSensor {
    private static final Logger LOG = LoggerFactory
            .getLogger(UnitTestWorkflowSensor.class);
    
    private static final String LOGPREFIX = "IntegrationTestWorkflowSensor : ";
    private VsTestEnvironment vsTestEnvironment;
    private WorkflowDirector workFlowDirector;
    public IntegrationTestWorkflowSensor(VsTestEnvironment vsTestEnvironment,
            MsCoverProperties msCoverProperties, FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            PathResolver pathResolver, WorkflowDirector workflowDirector) {
        super(vsTestEnvironment, msCoverProperties, fileSystem,
                microsoftWindowsEnvironment, pathResolver, workflowDirector);
        this.vsTestEnvironment = vsTestEnvironment;
        this.workFlowDirector = workflowDirector;
    }

    @Override
    void analyse(Project project, SensorContext context) {
        LogInfo("Starting");
        LogChanger.setPattern();
        DefaultPicoContainer childContainer = prepareChildContainer(context);
        childContainer.addComponent(VsTestIntegrationTestWorkflowSteps.class);
        vsTestEnvironment.setCoverageXmlFile(project, "coverage-report.xml");
        workFlowDirector.wire(childContainer);
        workFlowDirector.execute();
        LogInfo("Done");
    }
    

    private void LogInfo(String msg, Object... objects) {
        LOG.info(LOGPREFIX + msg, objects);
    }
}
