package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.IntegrationTestCache;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.OpenCoverSpecFlowTestRunnerSensor;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SensorAnalyzeTest {

	@Mock private Settings settings;
	@Mock private FileSystem fileSystem ;
	@Mock private MsCoverConfiguration configuration;
	@Mock private TestCache cache;
	@Mock private CachedSpecflowIntegrationTestRunner runner;
	private OpenCoverSpecFlowTestRunnerSensor sensor;
	@Mock private Project module;
	@Mock private SensorContext context;
	@Mock private TestResultsBuilder testResultsBuilder;
	@Mock private VsTestTestResultsSaver testResultsSaver;
	@Mock private CoverageReader coverageReader;
	@Mock private CoverageSaver coverageSaver;
	@Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	@Mock private IntegrationTestsConfiguration integrationTestsConfiguration;
	@Mock private OpenCoverModuleSaver openCoverModuleSaver;
	@Mock private IntegrationTestCache integrationTestCache;
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);	

		sensor = new OpenCoverSpecFlowTestRunnerSensor(runner,testResultsSaver,integrationTestsConfiguration,fileSystem);
	}
	
	@Test
	public void test() {
		String projectName="projectName";
		File root = new File("root");
		when(fileSystem.workDir()).thenReturn(new File("workdir"));
		when(runner.getTestResults()).thenReturn(new ProjectUnitTestResults());
		when(integrationTestsConfiguration.getDirectory()).thenReturn(root);
		when(module.getName()).thenReturn(projectName);
		when(runner.setCoverageFile(any(File.class))).thenReturn(runner);
		when(runner.setCoverageRoot(any(File.class))).thenReturn(runner);
		sensor.analyse(module, context);
	}
}
