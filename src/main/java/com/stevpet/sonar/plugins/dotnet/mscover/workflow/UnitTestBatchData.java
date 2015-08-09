package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class UnitTestBatchData implements BatchExtension {
    private File testResults ;
    private File testCoverage ;
    private boolean hasRun;
    
    public File getTestResults() {
        return testResults;
    }

    public File getTestCoverage() {
        return testCoverage;
    }

    public boolean hasRun() {
        return hasRun;
    }
    public void setHasRun(File coverageFile, File testResultsFile) {
        this.testCoverage=coverageFile;
        this.testResults=testResultsFile;
        hasRun=true;
    }
    
}
