package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;

import static org.mockito.Mockito.when;

import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.plugin.MsCoverPlugin;

public class UnitTestWorkflowSensorCreationSociableTest {

	@Mock Project project;
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		when(project.isRoot()).thenReturn(false);
	}
	
	@Ignore
    @Test
    public void ResolveUnitTestWorkflowSensor() {
        DefaultPicoContainer container = composeDependencies();
        try {
            container.getComponent(UnitTestWorkflowSensor.class);
        } catch (Exception e) {
            fail("not able to instantiate the sensor\n" + e.toString());
        }
    }

    private DefaultPicoContainer composeDependencies() {
        MsCoverPlugin plugin = new MsCoverPlugin();
        List<?> extensions = plugin.getExtensions();
        DefaultPicoContainer container = new DefaultPicoContainer();
        for (Object extension : extensions) {
            if (!(extension instanceof PropertyDefinition)) {
                container.addComponent(extension);
            }
        }
        container.addComponent(project).addComponent(PathResolver.class).addComponent(DefaultFileSystem.class).addComponent(Settings.class);
        return container;
    }

}
