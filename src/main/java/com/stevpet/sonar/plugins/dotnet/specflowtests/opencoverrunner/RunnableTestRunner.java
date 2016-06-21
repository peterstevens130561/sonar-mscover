package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

class RunnableTestRunner implements Runnable {
    private Logger LOG = LoggerFactory.getLogger(RunnableTestRunner.class);
    private IntegrationTestRunner testRunner ;
    private String projectName;
    private Map<String, ProjectUnitTestResults> testResultsMap;
    
    RunnableTestRunner(IntegrationTestRunner testRunner, String projectName, Map<String, ProjectUnitTestResults> testResultsMap) {
        this.testRunner = testRunner;
        this.projectName=projectName;
        this.testResultsMap=testResultsMap;
    }
    
    @Override
    public void run() {

        testRunner.execute();
        ProjectUnitTestResults testResults = testRunner.getTestResults();
        synchronized(testResultsMap) {
            testResultsMap.put(projectName,testResults);
        }
        LOG.info("+++ tests on project {}",projectName);
    }
    
}
