package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;

public class VsTestTestResultsSaverTest {

	@Mock private PathResolver pathResolver;
	@Mock private FileSystem fileSystem;
	@Mock private DefaultResourceResolver resourceResolver;
	@Mock private TestResultsFormatter testResultsFormatter;
	private VsTestTestResultsSaver saver;
	@Mock private SensorContext sensorContext;
	@Mock private ProjectUnitTestResults projectUnitTestResults;

	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void instantion() {
		try {
			VsTestTestResultsSaver.create(pathResolver,fileSystem);
		} catch (Exception e) {
			fail("could not instantiate");
		}
	}
	
	@Test 
	public void noResults() {
		saver= new VsTestTestResultsSaver(resourceResolver,testResultsFormatter);
		when(projectUnitTestResults.values()).thenReturn(new ArrayList<ClassUnitTestResult>());
		saver.save(sensorContext, projectUnitTestResults);

		verify(sensorContext,times(0)).saveMeasure(any(Resource.class),any(Metric.class),any(Double.class));
		verify(sensorContext,times(0)).saveMeasure(any(Resource.class),any(Measure.class));
		verify(sensorContext,times(0)).saveMeasure(any(Metric.class), any(Double.class));
	}
	
	@Test 
	public void one() {
		ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
		File sourceFile = new File("source.cs");
		org.sonar.api.resources.File resourceFile=org.sonar.api.resources.File.create("source.cs");
		ClassUnitTestResult result = projectUnitTestResults.addFile(sourceFile);
		
		when(resourceResolver.getFile(sourceFile)).thenReturn(resourceFile);
		
		saver= new VsTestTestResultsSaver(resourceResolver,testResultsFormatter);

		saver.save(sensorContext, projectUnitTestResults);

		verify(sensorContext,times(6)).saveMeasure(any(Resource.class),any(Metric.class),any(Double.class));
		verify(sensorContext,times(1)).saveMeasure(any(Resource.class),any(Measure.class));
		verify(sensorContext,times(0)).saveMeasure(any(Metric.class), any(Double.class));
	}
}
