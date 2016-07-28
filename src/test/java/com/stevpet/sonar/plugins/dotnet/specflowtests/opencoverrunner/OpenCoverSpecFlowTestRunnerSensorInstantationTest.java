package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.SonarPlugin;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.plugin.MsCoverPlugin;



public class OpenCoverSpecFlowTestRunnerSensorInstantationTest {

	@Mock Settings settings ;
	@Mock Project project;
	@Mock DefaultFileSystem fs;
	@Mock ResourcePerspectives resourcePerspectives;
	DefaultPicoContainer picoContainer;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		picoContainer = new DefaultPicoContainer() ;
		picoContainer.addComponent( settings);
		picoContainer.addComponent(resourcePerspectives);
		SonarPlugin plugin = new MsCoverPlugin();
		for(Object clazz : plugin.getExtensions()) {

		    if( !clazz.getClass().isAssignableFrom(PropertyDefinition.class)){
		        picoContainer.addComponent(clazz);
		    }
		}
		picoContainer.addComponent(new PathResolver());
		
	}
	
	@Test
	public void instantiateSaver() {
		picoContainer.addComponent(fs).addComponent(project);
		picoContainer.getComponent(OpenCoverSpecFlowTestSaverSensor.class);		
	}
	
	@Test
	public void instantiateRunner() {
		picoContainer.addComponent(fs).addComponent(project);
		picoContainer.getComponent(OpenCoverSpecFlowTestRunnerSensor.class);		
	}
}
