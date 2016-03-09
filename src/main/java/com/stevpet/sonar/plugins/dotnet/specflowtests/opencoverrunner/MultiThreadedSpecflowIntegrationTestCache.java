package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.HashMap;
import java.util.Map;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

@InstantiationStrategy(value=InstantiationStrategy.PER_BATCH)
public class MultiThreadedSpecflowIntegrationTestCache implements BatchExtension {

    protected boolean didExecute;
    public boolean isDidExecute() {
        return didExecute;
    }

    public void setDidExecute(boolean didExecute) {
        this.didExecute = didExecute;
    }

    private Map<String,ProjectUnitTestResults> testResultsMap = new HashMap<>();

    public ProjectUnitTestResults getTestResults(String module) {
        synchronized(testResultsMap) {
        return testResultsMap.get(module);
        }
    }

    public boolean getDidExecute() {
        return didExecute;
    }

    public void put(String projectName, ProjectUnitTestResults testResults) {
        synchronized(testResultsMap) {
            testResultsMap.put(projectName, testResults);
        }
    }

}