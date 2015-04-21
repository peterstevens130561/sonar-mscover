package com.stevpet.sonar.plugins.dotnet.mscover.workflow;


import org.jfree.util.Log;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class DefaultTestResultsSaver implements TestResultsSaver {

	TestResultsSaver testResultsSaver ;
	SensorContext sensorContext;
	private TestResultsFormatter testResultsFormatter;
	private ResourceResolver resourceResolver;

	public DefaultTestResultsSaver(SensorContext sensorContext,ResourceResolver resourceResolver,TestResultsFormatter testResultsFormatter) {
		this.sensorContext = sensorContext;
		this.testResultsFormatter = testResultsFormatter;
		this.resourceResolver = resourceResolver;
	}

	@Override
	public void save(ProjectUnitTestResults projectUnitTestResults) {
		for(ClassUnitTestResult classUnitTestResult: projectUnitTestResults.values()) {
			java.io.File file = classUnitTestResult.getFile();
			if(file==null) {
				continue;
			}
			File sonarFile=resourceResolver.getFile(file);
			if(sonarFile==null) {
				Log.warn("Could not find resource for " + file.getAbsolutePath());
				continue;				
			}
			saveFileSummaryResults(classUnitTestResult,sonarFile);
			saveFileTestResults(classUnitTestResult,sonarFile);
		}
	}
	
	public void saveFileSummaryResults(ClassUnitTestResult fileResults,
			File sonarFile) {
		sensorContext.saveMeasure(sonarFile,CoreMetrics.SKIPPED_TESTS, (double) 0);
		sensorContext.saveMeasure(sonarFile,CoreMetrics.TEST_ERRORS, (double) 0);
		sensorContext.saveMeasure(sonarFile,CoreMetrics.TEST_SUCCESS_DENSITY,
				fileResults.getDensity() * 100.0);
		sensorContext.saveMeasure(sonarFile,CoreMetrics.TEST_FAILURES,
				fileResults.getFail());
		sensorContext.saveMeasure(sonarFile,CoreMetrics.TEST_EXECUTION_TIME, 1000.0);
		sensorContext.saveMeasure(sonarFile,CoreMetrics.TESTS, fileResults.getTests());
	}

	public void saveFileTestResults(ClassUnitTestResult fileResults,
			File sonarFile) {
		String data = testResultsFormatter.formatClassUnitTestResults(fileResults);
		Measure testData = new Measure(CoreMetrics.TEST_DATA, data);
		testData.setPersistenceMode(PersistenceMode.DATABASE);
		sensorContext.saveMeasure(sonarFile, testData);
	}

}
