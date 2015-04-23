package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.ResourceResolver;

public class DefaultBranchFileCoverageSaverTest {
	private static final String FIRST_FILE = "a/b";
	private SonarCoverage coverage = new SonarCoverage();
	
	@Mock ResourceResolver resourceResolver ;
	@Mock SensorContext sensorContext;
	private BranchFileCoverageSaver coverageSaver ;
	File coveredResource;
	java.io.File coveredFile;
	
	@Captor
	private ArgumentCaptor<Metric> metricsArgument;
	@Captor
	private ArgumentCaptor<Double> valueArgument;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		coverageSaver = new DefaultBranchFileCoverageSaver(resourceResolver, sensorContext);
		coveredFile = new java.io.File(FIRST_FILE);
		coveredResource = File.create(FIRST_FILE);
		when(resourceResolver.getFile(eq(coveredFile))).thenReturn(coveredResource);
		givenCoveredFile();
		CoverageLinePoints coveragePoints=coverage.getCoveredFile("1").getBranchPoints();
		coverageSaver.saveMeasures(coveragePoints, coveredFile);
	}
	
	@Test
	public void resourceResolver_invoked() {
		verify(resourceResolver,times(1)).getFile(coveredFile);
	}
	
	@Test
	public void saveMeasureCoverage_invoked()
	{
		verify(sensorContext,times(2)).saveMeasure(eq(coveredResource),any(Measure.class));
	}
	
	@Test
	public void saveMeasure_FourTimes_invokedWithCorrectValues() {
		verify(sensorContext,times(3)).saveMeasure(eq(coveredResource),metricsArgument.capture(),valueArgument.capture());
		assertEquals("uncovered_conditions",metricsArgument.getAllValues().get(0).getKey());
		assertEquals("uncovered conditions", 2.0,valueArgument.getAllValues().get(0).doubleValue(),0.1);
		
		assertEquals("conditions_to_cover",metricsArgument.getAllValues().get(1).getKey());
		assertEquals("conditions to cover", 3.0,valueArgument.getAllValues().get(1).doubleValue(),0.1);
		
		assertEquals("branch_coverage",metricsArgument.getAllValues().get(2).getKey());
		assertEquals("branch coverage", 33.0,valueArgument.getAllValues().get(2).doubleValue(),0.1);
		
	}
	
	private void givenCoveredFile() {	
		SonarFileCoverage fileCoverage=coverage.getCoveredFile("1");
		fileCoverage.addBranchPoint(3,true);
		fileCoverage.addBranchPoint(4,false);
		fileCoverage.addBranchPoint(5,false);
		fileCoverage.setAbsolutePath(FIRST_FILE);
	}
}
