package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.jfree.util.Log;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.plugin.MsCoverPlugin;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.MsCoverPlugingetExtensions;

public class IntegrationTestWorkflowSensorTest {

    
    Sensor sensor ;
    private DefaultPicoContainer picoContainer;
    @Mock private Project module;
    @Mock private SensorContext context; 
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        picoContainer = new DefaultPicoContainer();
        List providedExtensions = Arrays.asList(PathResolver.class,DefaultFileSystem.class,Settings.class);
        List extensions= new MsCoverPlugin().getExtensions();
        extensions.addAll(providedExtensions);
        for(Object extension : extensions) {
            try {
            picoContainer.addComponent(extension);
            } catch(PicoCompositionException e) {
                Log.error(e.getMessage());
            }
        }
        sensor = picoContainer.getComponent(IntegrationTestWorkflowSensor.class);
    }
    

    @Ignore
    @Test
    public void createDependencies() {
        try {
        sensor.analyse(module, context);
        } catch (Exception e) {
            fail("all this test does is to ensure that the components needed are created");
        }
        
    }
    

}
