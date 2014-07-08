package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestFilesResultRegistry.ForEachUnitTestFile;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.NullResource;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class TestSaver {

    private static final Logger LOG = LoggerFactory
            .getLogger(TestSaver.class);
    
    private final SensorContext context;
    private SourceFileNamesRegistry sourceFileNamesRegistry ;
    private UnitTestFilesResultRegistry unitTestFilesResultRegistry;
    private SourceFilePathHelper sourceFilePathHelper;
    private StringBuilder testCaseDetails ;
    private ResultsModel  projectSummaryResults;
    private MeasureSaver measureSaver;
    private ResourceMediator resourceMediator;
    
    public TestSaver(SensorContext context,ResourceMediator resourceMediator,MeasureSaver measureSaver) {
        this.context = context;
        this.resourceMediator = resourceMediator;
        this.measureSaver = measureSaver;
    }
    public SourceFileNamesRegistry getSourceFileNamesRegistry() {
        return sourceFileNamesRegistry;
    }

    public void setSourceFileNamesRegistry(
            SourceFileNamesRegistry sourceFileNamesRegistry) {
        this.sourceFileNamesRegistry = sourceFileNamesRegistry;
    }

    public UnitTestFilesResultRegistry getUnitTestFilesResultRegistry() {
        return unitTestFilesResultRegistry;
    }

    public void setUnitTestFilesResultRegistry(
            UnitTestFilesResultRegistry unitTestFilesResultRegistry) {
        this.unitTestFilesResultRegistry = unitTestFilesResultRegistry;
    }


    public void setSourceFilePathHelper(SourceFilePathHelper sourceFilePathHelper) {
        this.sourceFilePathHelper = sourceFilePathHelper;
    }

    /**
     * Save results in all test files
     */
    public void save() {
        projectSummaryResults = new ResultsModel();
        unitTestFilesResultRegistry.forEachUnitTestFile(new SaveUnitTestFileMeasures());
        saveProjectTestResults();
    }

    private void saveProjectTestResults() {
        LOG.info("ResultsSensor: {}",projectSummaryResults.getExecutedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TESTS,(double)projectSummaryResults.getExecutedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TEST_FAILURES,(double)projectSummaryResults.getFailedTests());
        measureSaver.saveSummaryMeasure(CoreMetrics.TEST_ERRORS,(double)projectSummaryResults.getErroredTests());
    }

    class SaveUnitTestFileMeasures implements ForEachUnitTestFile {

        public void execute(String fileID, UnitTestFileResultModel fileResults) {
        ResourceSeam sonarFile = tryToGetUnitTestResource(fileID);
        if(!(sonarFile instanceof NullResource)) {
            projectSummaryResults.add(fileResults);
        }
        saveSummaryMeasures(fileResults, sonarFile);
        saveTestCaseMeasures(fileResults, sonarFile);

    }

        private ResourceSeam tryToGetUnitTestResource(
                String fileID) {
            String sourceFileName=sourceFileNamesRegistry.getSourceFileName(fileID);

            File sourceFile = sourceFilePathHelper.getCanonicalFile(sourceFileName);
            if(sourceFile == null) {
                LOG.warn("Could not get unit test file for file "+sourceFileName);
                return new NullResource();
            }
            return resourceMediator.getSonarTestResource(sourceFile);
        }
   

}


    public void saveSummaryMeasures( UnitTestFileResultModel fileResults,
            ResourceSeam sonarFile) {
        sonarFile.saveMetricValue(CoreMetrics.SKIPPED_TESTS, (double)0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_ERRORS,(double) 0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_SUCCESS_DENSITY,fileResults.getDensity()*100.0);
        sonarFile.saveMetricValue(CoreMetrics.TEST_FAILURES,fileResults.getFail());
        sonarFile.saveMetricValue(CoreMetrics.TEST_EXECUTION_TIME,1000.0);
        sonarFile.saveMetricValue(CoreMetrics.TESTS,fileResults.getTests());  
    }

    public void saveTestCaseMeasures( UnitTestFileResultModel fileResults,
            ResourceSeam sonarFile) {
        testCaseDetails = new StringBuilder(256);
        testCaseDetails.append("<tests-details>");
        List<UnitTestResultModel> details = fileResults.getUnitTests();
        for (UnitTestResultModel detail : details) {
          testCaseDetails.append("<testcase status=\"" +getSonarStatus(detail.getOutcome()) + "\"");
          testCaseDetails.append(" time=\"0\"");
          testCaseDetails.append(" name=\"");
          testCaseDetails.append(detail.getTestName());
          testCaseDetails.append("\">");
          if(isNotPassed(detail)) {
              testCaseDetails.append("<error ");
              testCaseDetails.append(getMessageAttribute(detail));
              testCaseDetails.append(">");
              testCaseDetails.append("<![CDATA[");
              testCaseDetails.append(StringEscapeUtils.escapeXml(detail.getStackTrace())).append("]]>");
              testCaseDetails.append("</error>");
          }
          testCaseDetails.append("</testcase>");
        }
        testCaseDetails.append("</tests-details>");
        String data=testCaseDetails.toString();
        Measure testData = new Measure(CoreMetrics.TEST_DATA, data);
        testData.setPersistenceMode(PersistenceMode.DATABASE);
        sonarFile.saveMeasure(testData);
        LOG.debug("test detail : {}", testCaseDetails);
    }

    private boolean isNotPassed(UnitTestResultModel detail) {
        return !"Passed".equals(detail.getOutcome());
    }

    private String getSonarStatus(String outcome) {
        if("Passed".equals(outcome)) {
            return "ok" ;
        } else {
            return "error" ;
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