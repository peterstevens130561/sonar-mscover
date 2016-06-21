package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.OpenCoverSpecFlowTestRunnerSensor;

public class OpenCoverSpecFlowTestRunnerSensorAnalyzeTest {

	@Mock private MsCoverConfiguration configuration;
	@Mock private TestCache cache;
	@Mock private IntegrationTestRunnerApplication runner;
	private OpenCoverSpecFlowTestRunnerSensor sensor;
	@Mock private Project module;
	@Mock private SensorContext sensorContext;
	@Mock private VsTestTestResultsSaver testResultsSaver;
	@Mock private IntegrationTestsConfiguration integrationTestsConfiguration;
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);	
        sensor = new OpenCoverSpecFlowTestRunnerSensor(runner,testResultsSaver,integrationTestsConfiguration);
	}
	
	@Test
	public void test() {
		String projectName="projectName";
		File root = new File("root");
		ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
		when(integrationTestsConfiguration.getDirectory()).thenReturn(root);
		when(module.getName()).thenReturn(projectName);
		when(runner.getTestResults(projectName)).thenReturn(projectUnitTestResults);
		sensor.analyse(module, sensorContext);
		
		
		verify(runner,times(1)).execute();
		verify(testResultsSaver,times(1)).save(eq(sensorContext), eq(projectUnitTestResults));
	}
	
}
