package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class CallableTestRunner implements Callable<Boolean>{

    private Logger LOG = LoggerFactory.getLogger(CallableTestRunner.class);
    private IntegrationTestRunner testRunner ;
    private String projectName;
    private Map<String, ProjectUnitTestResults> testResultsMap;
    
    CallableTestRunner(IntegrationTestRunner testRunner, String projectName, Map<String, ProjectUnitTestResults> testResultsMap) {
        this.testRunner = testRunner;
        this.projectName=projectName;
        this.testResultsMap=testResultsMap;
    }
    

    @Override
    public Boolean call() throws Exception {

        testRunner.execute();
        ProjectUnitTestResults testResults = testRunner.getTestResults();
        synchronized(testResultsMap) {
            testResultsMap.put(projectName,testResults);
        }
        LOG.info("+++ tests on project {}",projectName);
        return true;
    }
}
