package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.CoreMetrics;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ResultsModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestFileResultModelMock;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeamMock;

public class TestResultsSaverTest {
    
    MeasureSaverMock measureSaver= new MeasureSaverMock();
    private TestResultsSaver saver;
    private ResultsModel projectSummaryResults = new ResultsModel();

    @Before
    public void before() {
        saver = new TestResultsSaver(measureSaver.getMock());      
    }
    
    @Test
    public void create_ExpectNotNull() {
        assertNotNull(saver);
    }
    
    @Test
    public void saveProjectResults_ShouldSave() {
        int errorredTests=5;
        
        projectSummaryResults.setErroredTests(errorredTests);
        int executedTests=4;
        projectSummaryResults.setExecutedTests(executedTests);
        int failedTests=3;
        projectSummaryResults.setFailedTests(failedTests);
        int passedTests=2;
        projectSummaryResults.setPassedTests(passedTests);
 
        saver.saveProjectTestResults(projectSummaryResults);
        
        measureSaver.verifySaveProjectSummary(CoreMetrics.TESTS,executedTests);
        measureSaver.verifySaveProjectSummary(CoreMetrics.TEST_FAILURES, failedTests);
        measureSaver.verifySaveProjectSummary(CoreMetrics.TEST_ERRORS, errorredTests);
        
    }
    
    @Test
    public void saveSummaryMeasures_ShouldSave() {
        UnitTestFileResultModelMock fileResults = new UnitTestFileResultModelMock();
        int failed=5;
        int density=20;
        int tests=4;
        
        fileResults.givenFailed(failed).givenDensity(density).givenTests(tests);
        ResourceSeamMock sonarFile = new ResourceSeamMock();
        saver.saveSummaryMeasures(fileResults.getMock(), sonarFile.getMock());
        
        sonarFile.verifySaveMetricValue(CoreMetrics.SKIPPED_TESTS,0);
        sonarFile.verifySaveMetricValue(CoreMetrics.TEST_ERRORS,0);
        sonarFile.verifySaveMetricValue(CoreMetrics.TEST_SUCCESS_DENSITY,density*100);
        sonarFile.verifySaveMetricValue(CoreMetrics.TEST_FAILURES,failed);
        sonarFile.verifySaveMetricValue(CoreMetrics.TESTS, tests);
    }
    
    @Test
    public void saveTestCaseMeasures_EmptyResults_MinimalDetail() {
        UnitTestFileResultModel fileResults = new UnitTestFileResultModel();
        ResourceSeamMock sonarFile = new ResourceSeamMock();
        saver.saveTestCaseMeasures(fileResults, sonarFile.getMock());
        String expected="<tests-details></tests-detailsX>";
        sonarFile.verifySaveMeasure(CoreMetrics.TEST_DATA,expected);
    }
}
