package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class UnitTestBatchData implements BatchExtension {
    private File testResults ;
    private File testCoverage ;
    
    public File getTestResults() {
        return testResults;
    }
    public void setTestResults(File testResults) {
        this.testResults = testResults;
    }
    public File getTestCoverage() {
        return testCoverage;
    }
    public void setTestCoverage(File testCoverage) {
        this.testCoverage = testCoverage;
    }
    
}
