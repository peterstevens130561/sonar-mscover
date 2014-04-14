package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.ArrayList;
import java.util.List;
/*
 *    saveFileMeasure(testFile, context, CoreMetrics.SKIPPED_TESTS, testReport.getSkipped());
        saveFileMeasure(testFile, context, CoreMetrics.TESTS, testsCount);
        saveFileMeasure(testFile, context, CoreMetrics.TEST_ERRORS, testReport.getErrors());
        saveFileMeasure(testFile, context, CoreMetrics.TEST_FAILURES, testReport.getFailures());
        saveFileMeasure(testFile, context, CoreMetrics.TEST_EXECUTION_TIME, testReport.getTimeMS());
        saveFileMeasure(testFile, context, TestMetrics.COUNT_ASSERTS, testReport.getAsserts());
 */
public class UnitTestFileResultModel {
    private String sourceFileID ;
    private int error ;
    
    private List<UnitTestResultModel> tests = new ArrayList<UnitTestResultModel>() ;
    
    public void add(UnitTestResultModel unitTest) {
        tests.add(unitTest);
        String outcome=unitTest.getOutcome();
        if(!"Pass".equals(outcome)) {
            error++;
        }
    }
    
}
