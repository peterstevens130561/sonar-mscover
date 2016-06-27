package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.resources.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.TestResultsSaver;

class DefaultTestResultsSaver implements TestResultsSaver {
    private SensorContext sensorContext;
    private TestResultsFormatter testResultsFormatter;
    private ResourceResolver resourceResolver;

    @SuppressWarnings("ucd")
    public DefaultTestResultsSaver(SensorContext sensorContext,
            DefaultResourceResolver resourceResolver,
            TestResultsFormatter testResultsFormatter) {
        this.sensorContext = sensorContext;
        this.testResultsFormatter = testResultsFormatter;
        this.resourceResolver = resourceResolver;
    }

    @Override
    public void save(ProjectUnitTestResults projectUnitTestResults) {
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
        }
    }

    private void saveFileSummaryResults(ClassUnitTestResult fileResults,
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
        Measure<?> testData = new Measure(CoreMetrics.TEST_DATA, data);
        testData.setPersistenceMode(PersistenceMode.DATABASE);
        sensorContext.saveMeasure(sonarFile, testData);
    }

}
