package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.BatchExtension;
import org.sonar.api.Extension;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.plugin.MsCoverPlugin;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.OpenCoverWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;

public class UnitTestWorkflowSensorCreationSociableTest {

    @Test
    public void ResolveUnitTestWorkflowSensor() {
        DefaultPicoContainer container = composeDependencies();
        try {
            container.getComponent(UnitTestWorkflowSensor.class);
        } catch (Exception e) {
            fail("not able to instantiate the sensor\n" + e.toString());
        }
    }

    @Test
    public void ExecuteUnitTestWorkflowSensor() {
        DefaultPicoContainer container = composeDependencies();
        container.addComponent(OpenCoverWorkflowSteps.class);
        WorkflowSteps steps=container.getComponent(OpenCoverWorkflowSteps.class);
        
    }
    private DefaultPicoContainer composeDependencies() {
        MsCoverPlugin plugin = new MsCoverPlugin();
        List extensions = plugin.getExtensions();
        DefaultPicoContainer container = new DefaultPicoContainer();
        for(Object extension:extensions) {
            container.addComponent(extension);
        }
        container.addComponent(PathResolver.class).addComponent(DefaultFileSystem.class).addComponent(Settings.class);
        return container;
    }
    
  
}
