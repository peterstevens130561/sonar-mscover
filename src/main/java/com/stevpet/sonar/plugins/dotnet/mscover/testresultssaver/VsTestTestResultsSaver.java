package com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.resources.File;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.DefaultTestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class VsTestTestResultsSaver implements BatchExtension{
    private static final Logger LOG = LoggerFactory
            .getLogger(VsTestTestResultsSaver.class);
    TestResultsSaver testResultsSaver;
    SensorContext sensorContext;
    private TestResultsFormatter testResultsFormatter;
    private ResourceResolver resourceResolver;

    @SuppressWarnings("ucd")
    /**
     * With all dependencies, 
     * 
     * @param resourceResolver
     * @param testResultsFormatter
     */
	public
    VsTestTestResultsSaver(
            DefaultResourceResolver resourceResolver,
            TestResultsFormatter testResultsFormatter) {

        this.testResultsFormatter = testResultsFormatter;
        this.resourceResolver = resourceResolver;
    }
    
    /**
     * The default one
     * @param pathResolver
     * @param filesystem
     */
	public static VsTestTestResultsSaver create(
			PathResolver pathResolver, FileSystem filesystem) {
		return new VsTestTestResultsSaver(
			new DefaultResourceResolver(pathResolver,filesystem), 
			new DefaultTestResultsFormatter()
		);
	}

    public void save(@Nonnull SensorContext sensorContext,@Nonnull ProjectUnitTestResults projectUnitTestResults) {
        this.sensorContext=sensorContext;
        int saved = 0;
    	for (ClassUnitTestResult classUnitTestResult : projectUnitTestResults
                .values()) {
            java.io.File file = classUnitTestResult.getFile();
            if (file == null) {
                continue;
            }
            File sonarFile = resourceResolver.getFile(file);
            if (sonarFile == null) {
                continue;
            }
            saveFileSummaryResults(classUnitTestResult, sonarFile);
            saveFileTestResults(classUnitTestResult, sonarFile);
            ++saved;
        }
    	LOG.debug("saved {} testresults",saved);
    }

    public void saveFileSummaryResults(ClassUnitTestResult fileResults,
            File sonarFile) {
        sensorContext.saveMeasure(sonarFile, CoreMetrics.SKIPPED_TESTS,
                fileResults.getIgnored());
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_ERRORS,
                (double) 0);
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_SUCCESS_DENSITY,
                fileResults.getDensity() * 100.0);
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_FAILURES,
                fileResults.getFail());
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TEST_EXECUTION_TIME,
                1000.0);
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TESTS,
                fileResults.getTests());
    }

    public void saveFileTestResults(ClassUnitTestResult fileResults,
            File sonarFile) {
        String data = testResultsFormatter
                .formatClassUnitTestResults(fileResults);
        Measure testData = new Measure(CoreMetrics.TEST_DATA, data);
        testData.setPersistenceMode(PersistenceMode.DATABASE);
        sensorContext.saveMeasure(sonarFile, testData);
    }

}