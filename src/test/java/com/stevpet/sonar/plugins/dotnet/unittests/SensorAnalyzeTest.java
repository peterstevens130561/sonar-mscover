package com.stevpet.sonar.plugins.dotnet.unittests;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SensorAnalyzeTest {

	@Mock private FileSystem fileSystem ;
	@Mock private MsCoverConfiguration configuration;
	@Mock private TestCache cache;
	@Mock private OpenCoverTestRunner runner;
	private OpenCoverUnitTestSensor sensor;
	@Mock private Project module;
	@Mock private SensorContext sensorContext;
	@Mock private TestResultsBuilder testResultsBuilder;
	@Mock private VsTestTestResultsSaver testResultsSaver;
	@Mock private CoverageReader coverageReader;
	@Mock private CoverageSaver coverageSaver;
	@Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	
	private File testResultsFile= new File("testResults");
	private File coverageFile = new File("workdir/coverage.xml");
	private ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);	

		sensor = new OpenCoverUnitTestSensor(fileSystem,configuration,cache,runner, 
				testResultsBuilder, testResultsSaver,coverageReader,coverageSaver,microsoftWindowsEnvironment);
			when(fileSystem.workDir()).thenReturn(new File("workdir"));
			when(runner.getTestResultsFile()).thenReturn(testResultsFile);
			when(cache.getTestCoverageFile()).thenReturn(coverageFile);
			when(cache.getTestResultsFile()).thenReturn(new File("testResults"));
			when(testResultsBuilder.parse(testResultsFile, coverageFile)).thenReturn(projectUnitTestResults);
		}
	
	@Test
	public void initialRunOnUnitTestProject() {

		givenIsFirstProject();
		givenIsUnitTestProject();	
		sensor.analyse(module, sensorContext);
		
		shouldRunTests();
		shouldSaveCoverage();
		shouldSaveTestResults();
	}


	
	@Test
	public void initialRunOnProject() {
		givenIsNormalProject();
		givenIsFirstProject();
		
		//when
		sensor.analyse(module, sensorContext);
		
		shouldRunTests();
		shouldSaveCoverage();
		shouldNotSaveTestResults();
	}

	private void givenIsFirstProject() {
		when(cache.gatHasRun()).thenReturn(false);
	}

	private void shouldRunTests() {
		verify(runner,times(1)).execute();
	}
	
	@Test
	public void laterRunOnProject() {
		givenIsNormalProject();
		givenIsLaterProject();
		
		sensor.analyse(module, sensorContext);
		
		shouldNotRunTests();
		shouldSaveCoverage();
		shouldNotSaveTestResults();
	}

	private void givenIsLaterProject() {
		when(cache.gatHasRun()).thenReturn(true);
	}

	private void givenIsNormalProject() {
		when(microsoftWindowsEnvironment.isUnitTestProject(module)).thenReturn(false);
	}
	
	@Test
	public void laterRunOnUnitTestProject() {
		
		// given
		givenIsUnitTestProject();
		givenIsLaterProject();
		
		sensor.analyse(module, sensorContext);
		
		shouldNotRunTests();		
		shouldSaveCoverage();
		shouldSaveTestResults();
	}

	private void givenIsUnitTestProject() {
		when(microsoftWindowsEnvironment.isUnitTestProject(module)).thenReturn(true);
	}

	private void shouldNotRunTests() {
		verify(runner,times(0)).execute();
	}

	private void shouldSaveTestResults() {
		verify(testResultsSaver,times(1)).save(sensorContext, projectUnitTestResults);
	}
	
	private void shouldNotSaveTestResults() {
		verify(testResultsSaver,times(0)).save(sensorContext, projectUnitTestResults);
	}
	private void shouldSaveCoverage() {
		verify(coverageSaver,times(1)).save(eq(sensorContext), any(SonarCoverage.class));
	}
	

}
