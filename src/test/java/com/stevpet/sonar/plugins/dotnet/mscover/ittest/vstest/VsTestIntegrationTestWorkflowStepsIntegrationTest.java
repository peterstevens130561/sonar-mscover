package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.picocontainer.DefaultPicoContainer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowSteps;


public class VsTestIntegrationTestWorkflowStepsIntegrationTest {

	@Mock private MsCoverProperties msCoverProperties;
	@Mock private SensorContext sensorContext;
	@Mock private ResourceResolver resourceResolver;
	@Mock private FileSystem fileSystem;
	@Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	
	@Captor
	private ArgumentCaptor<Metric> metricsArgument;
	@Captor
	private ArgumentCaptor<Double> valueArgument;
	
	private VsTestEnvironment vsTestEnvironment ;
	
	DefaultPicoContainer container;
	WorkflowDirector director;
	
	WorkflowSteps workflow;
	@Before
	public void before() {
		container = new DefaultPicoContainer();
		vsTestEnvironment = new VsTestEnvironment();
	
		MockitoAnnotations.initMocks(this);
        workflow = new VsTestIntegrationTestWorkflowSteps();	
		container.addComponent(msCoverProperties)
		.addComponent(sensorContext)
		.addComponent(resourceResolver)
		.addComponent(vsTestEnvironment)
		.addComponent(fileSystem)
		.addComponent(microsoftWindowsEnvironment)
		.addComponent(workflow);
		
		director = new DefaultDirector();					
	}
	
	@Test
	public void IntegrationTest() {
		//given a single xml file
		File testFile=TestUtils.getResource("coverage.xml");
		//given the property points to this file
		when(msCoverProperties.getIntegrationTestsPath()).thenReturn(testFile.getAbsolutePath());
		
		List<String> artifactNames = new ArrayList<String>();
		when(microsoftWindowsEnvironment.getArtifactNames()).thenReturn( artifactNames);
		director.wire(container);
		director.execute();

		verify(sensorContext,times(8)).saveMeasure(any(Resource.class),metricsArgument.capture(),valueArgument.capture());
		assertEquals("it_lines_to_cover",metricsArgument.getAllValues().get(0).getKey());
		assertEquals("number of lines", 18.0,valueArgument.getAllValues().get(0).doubleValue(),0.1);

		assertEquals("it_uncovered_lines",metricsArgument.getAllValues().get(1).getKey());
		assertEquals("uncovered lines", 1.0,valueArgument.getAllValues().get(1).doubleValue(),0.1);

		assertEquals("it_coverage",metricsArgument.getAllValues().get(2).getKey());
		assertEquals("coverage", 94.0,valueArgument.getAllValues().get(2).doubleValue(),0.1);

		assertEquals("it_line_coverage",metricsArgument.getAllValues().get(3).getKey());
		assertEquals("line coverage", 94.0,valueArgument.getAllValues().get(3).doubleValue(),0.1);

	}
	
}
