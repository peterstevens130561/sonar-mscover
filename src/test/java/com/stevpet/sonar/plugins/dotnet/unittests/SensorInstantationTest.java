package com.stevpet.sonar.plugins.dotnet.unittests;

import org.junit.Before;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.SonarPlugin;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;


public class SensorInstantationTest {

	@Mock Settings settings ;
	@Mock Project project;
	DefaultPicoContainer picoContainer;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		picoContainer = new DefaultPicoContainer() ;
		picoContainer.addComponent( settings);
		SonarPlugin plugin = new OpenCoverPlugin();
		for(Object clazz : plugin.getExtensions()) {
			picoContainer.addComponent(clazz);
		}
		
	}
	
	public void instantiate() {
		picoContainer.addComponent(DefaultFileSystem.class).addComponent(project);
		Sensor sensor = picoContainer.getComponent(Sensor.class);		
	}
}
