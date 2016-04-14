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
        LOG.info("+++ tests on project {} started",projectName);
        testRunner.execute();
        ProjectUnitTestResults testResults = testRunner.getTestResults();
        LOG.info("+++ tests on project {} waiting to for access to coverage map",projectName);
        synchronized(testResultsMap) {
            LOG.info("+++ tests on project {} writing coverage map",projectName);
            testResultsMap.put(projectName,testResults);
        }
        LOG.info("+++ tests on project {} completed",projectName);
        return true;
        } catch (Exception e) {
            LOG.error("Failed to execute tests on project {}\n message {}\n{}",projectName, e.getLocalizedMessage(),e.getStackTrace().toString());
            throw e;
        }
    }
}
