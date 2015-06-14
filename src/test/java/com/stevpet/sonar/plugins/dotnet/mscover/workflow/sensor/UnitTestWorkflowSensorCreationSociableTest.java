package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.plugin.MsCoverPlugin;

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
        container.addComponent(UnitTestWorkflowSensor.class);
        WorkflowSensor sensor=container.getComponent(UnitTestWorkflowSensor.class);
        sensor.resolve();
        
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
