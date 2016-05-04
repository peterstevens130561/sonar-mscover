package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.stevpet.sonar.plugins.common.commandexecutor.TimeoutException;
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
            LOG.debug("+++ tests on project {} started", projectName);
            testRunner.execute();
            ProjectUnitTestResults testResults = testRunner.getTestResults();
            synchronized (testResultsMap) {
                LOG.info("+++ tests on project {} writing coverage map", projectName);
                testResultsMap.put(projectName, testResults);
            }
            LOG.debug("+++ tests on project {} completed", projectName);
            return true;

        } catch (TimeoutException e) {
            LOG.error("Timeout occurred during {}",projectName);
            return false;
        }
            catch (Exception e) {
            LOG.error("Failed to execute tests on project {}\n message {}\n{}", projectName, e.getLocalizedMessage(), e
                    .getStackTrace().toString());
            throw e;
        }
    }
    
}
