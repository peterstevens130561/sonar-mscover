package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class TestResultsSaver {

    private MeasureSaver measureSaver;

    public TestResultsSaver(MeasureSaver measureSaver) {
        this.measureSaver = measureSaver;
    }

    void saveProjectTestResults(ResultsModel projectSummaryResults) {
        measureSaver.saveSummaryMeasure(CoreMetrics.TESTS,
                (double) projectSummaryResults.getExecutedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TEST_FAILURES,
                (double) projectSummaryResults.getFailedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TEST_ERRORS,
                (double) projectSummaryResults.getErroredTests());
    }

    public void saveSummaryMeasures(UnitTestFileResultModel fileResults,
            ResourceSeam sonarFile) {
        sonarFile.saveMetricValue(CoreMetrics.SKIPPED_TESTS, (double) 0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_ERRORS, (double) 0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_SUCCESS_DENSITY,
                fileResults.getDensity() * 100.0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_FAILURES,
                fileResults.getFail());
        sonarFile.saveMetricValue(CoreMetrics.TEST_EXECUTION_TIME, 1000.0);
        sonarFile.saveMetricValue(CoreMetrics.TESTS, fileResults.getTests());
    }

    public void saveTestCaseMeasures(UnitTestFileResultModel fileResults,
            ResourceSeam sonarFile) {
        StringBuilder testCaseDetails = new StringBuilder(256);
        testCaseDetails.append("<tests-details>");
        List<UnitTestResultModel> details = fileResults.getUnitTests();
        for (UnitTestResultModel detail : details) {
            testCaseDetails.append("<testcase status=\""
                    + getSonarStatus(detail.getOutcome()) + "\"");
            testCaseDetails.append(" time=\"0\"");
            testCaseDetails.append(" name=\"");
            testCaseDetails.append(detail.getTestName());
            testCaseDetails.append("\">");
            if (isNotPassed(detail)) {
                testCaseDetails.append("<error ");
                testCaseDetails.append(getMessageAttribute(detail));
                testCaseDetails.append(">");
                testCaseDetails.append("<![CDATA[");
                testCaseDetails.append(
                        StringEscapeUtils.escapeXml(detail.getStackTrace()))
                        .append("]]>");
                testCaseDetails.append("</error>");
            }
            testCaseDetails.append("</testcase>");
        }
        testCaseDetails.append("</tests-details>");
        String data = testCaseDetails.toString();
        Measure testData = new Measure(CoreMetrics.TEST_DATA, data);
        testData.setPersistenceMode(PersistenceMode.DATABASE);
        sonarFile.saveMeasure(testData);
    }

    private boolean isNotPassed(UnitTestResultModel detail) {
        return !"Passed".equals(detail.getOutcome());
    }

    private String getSonarStatus(String outcome) {
        if ("Passed".equals(outcome)) {
            return "ok";
        } else {
            return "error";
        }
    }

    private String getMessageAttribute(UnitTestResultModel result) {
        String errorMessage = result.getMessage();
        String xmlErrorMessage = StringEscapeUtils.escapeXml(errorMessage);
        StringBuilder sb = new StringBuilder();
        sb.append("message=\"");
        sb.append(xmlErrorMessage);
        sb.append("\"");
        return sb.toString();
    }
}