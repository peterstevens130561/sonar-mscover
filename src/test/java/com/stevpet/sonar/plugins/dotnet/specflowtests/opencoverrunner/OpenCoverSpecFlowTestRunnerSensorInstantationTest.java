package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.SonarPlugin;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;



public class OpenCoverSpecFlowTestRunnerSensorInstantationTest {

	@Mock Settings settings ;
	@Mock Project project;
	DefaultPicoContainer picoContainer;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		picoContainer = new DefaultPicoContainer() ;
		picoContainer.addComponent( settings);
		SonarPlugin plugin = new OpenCoverSpecFlowPlugin();
		for(Object clazz : plugin.getExtensions()) {
			picoContainer.addComponent(clazz);
		}
		picoContainer.addComponent(new PathResolver());
		
	}
	
	@Test
	public void instantiateSaver() {
		picoContainer.addComponent(DefaultFileSystem.class).addComponent(project);
		Sensor sensor = picoContainer.getComponent(OpenCoverSpecFlowTestSaverSensor.class);		
	}
	
	@Test
	public void instantiateRunner() {
		picoContainer.addComponent(DefaultFileSystem.class).addComponent(project);
		Sensor sensor = picoContainer.getComponent(OpenCoverSpecFlowTestRunnerSensor.class);		
	}
}
