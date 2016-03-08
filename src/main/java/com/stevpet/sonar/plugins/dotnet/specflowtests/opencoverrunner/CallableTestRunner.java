package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public class CallableTestRunner implements Callable<Boolean> {

    private Logger LOG = LoggerFactory.getLogger(CallableTestRunner.class);
    private IntegrationTestRunner testRunner;
    private String projectName;
    private MultiThreadedSpecflowIntegrationTestCache testResultsMap;

    CallableTestRunner(IntegrationTestRunner testRunner, String projectName,
            MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecFlowIntegrationTestCache) {
        this.testRunner = testRunner;
        this.projectName = projectName;
        this.testResultsMap = multiThreadedSpecFlowIntegrationTestCache;
    }

    @Override
    public Boolean call() throws Exception {
        try {
        testRunner.execute();
        ProjectUnitTestResults testResults = testRunner.getTestResults();
        synchronized(testResultsMap) {
            testResultsMap.put(projectName,testResults);
        }
        LOG.info("+++ tests on project {} completed",projectName);
        return true;
        } catch (Exception e) {
            LOG.error("Failed to execute tests on project {} message {}",projectName, e.getLocalizedMessage());
            throw e;
        }
    }
}
