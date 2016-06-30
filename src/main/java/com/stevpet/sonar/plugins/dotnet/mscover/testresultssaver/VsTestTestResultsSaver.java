package com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.resources.File;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.test.MutableTestPlan;
import org.sonar.api.test.TestCase;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
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
    private ResourceResolver resourceResolver;
    private ResourcePerspectives perspectives;

    @SuppressWarnings("ucd")
    /**
     * With all dependencies, 
     * 
     * @param resourceResolver
     * @param testResultsFormatter
     */
	public
    VsTestTestResultsSaver(
            DefaultResourceResolver resourceResolver,ResourcePerspectives perspectives) {
        this.resourceResolver = resourceResolver;
        this.perspectives = perspectives;
    }
    
    /**
     * The default one
     * @param pathResolver
     * @param filesystem
     */
	public static VsTestTestResultsSaver create(
			PathResolver pathResolver, FileSystem filesystem, ResourcePerspectives perspectives) {
		return new VsTestTestResultsSaver(
			new DefaultResourceResolver(pathResolver,filesystem), perspectives
		);
	}

    public void save(@Nonnull SensorContext sensorContext,@Nonnull ProjectUnitTestResults projectUnitTestResults) {
        this.sensorContext=sensorContext;
        int saved = 0;
    	for (ClassUnitTestResult classUnitTestResult : projectUnitTestResults.values()) {
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
            saved += classUnitTestResult.getTests();
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
                fileResults.getLocalTimeMillis());
        sensorContext.saveMeasure(sonarFile, CoreMetrics.TESTS,
                fileResults.getTests());
    }

 
    public void saveFileTestResults(ClassUnitTestResult fileResults, File sonarFile) {
        for( UnitTestMethodResult result : fileResults.getUnitTests()) {
            MutableTestPlan testplan = perspectives.as(MutableTestPlan.class, sonarFile);
            if(testplan == null) {
                continue;
            }
            testplan.addTestCase(result.getTestName())
            .setDurationInMs(result.getTimeInMicros()/1000)
            .setMessage(result.getMessage())
            .setType(TestCase.TYPE_UNIT);
        }
    }

}