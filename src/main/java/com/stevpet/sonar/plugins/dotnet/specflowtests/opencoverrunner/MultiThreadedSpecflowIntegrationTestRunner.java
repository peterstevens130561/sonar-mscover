package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

/**
 * Starts a thread for each project, and waits for the completion of all tests
 * @author stevpet
 *
 */
public class MultiThreadedSpecflowIntegrationTestRunner implements IntegrationTestRunnerApplication {
    private static Logger LOG = LoggerFactory.getLogger(MultiThreadedSpecflowIntegrationTestRunner.class);
    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private final IntegrationTestsConfiguration integrationTestsConfiguration;
    private ExecutorService executorService;
    private boolean didExecute;

    private  Map<String,ProjectUnitTestResults> testResultsMap = new HashMap<>();
    private final FileSystem fileSystem;
    private IntegrationTestRunnerFactory testRunnerFactory;
    public MultiThreadedSpecflowIntegrationTestRunner(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            IntegrationTestsConfiguration integrationTestsConfiguration,
            IntegrationTestRunnerFactory testRunnerFactory,
            FileSystem fileSystem) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.integrationTestsConfiguration=integrationTestsConfiguration;
        this.testRunnerFactory=testRunnerFactory;
        this.fileSystem=fileSystem;
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.IntegrationTestRunnerApplication#getTestResults()
     */
    @Override
    public ProjectUnitTestResults getTestResults(String module) {
        synchronized(testResultsMap) {
            LOG.debug("Getting results for {}",module);
            return testResultsMap.get(module);
        }
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.IntegrationTestRunnerApplication#execute()
     */
    @Override
    public void execute() {
        if(didExecute) {
            LOG.debug("Using previous data");
            return;
        }

        int threads = integrationTestsConfiguration.getTestRunnerThreads();
        int timeout = integrationTestsConfiguration.getTestRunnerTimeout();
        executorService = Executors.newFixedThreadPool(threads);
        LOG.debug("Using {} threads",threads);
        List<Future<Boolean>> results = queueTests();
        waitTillDone(timeout, results);
        didExecute=true;
    }


    private void waitTillDone(int timeout, List<Future<Boolean>> results) {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(timeout, TimeUnit.MINUTES)) {
                throw new SonarException("Timeout occurred during parsing of coveragefiles");
            }
            for(Future<Boolean> result:results) {
                result.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            LOG.error("Execution of tests failed {}",e.getCause().toString());
            throw new SonarException("Execution of tests failed, see inner exception",e.getCause());
        }
    }


    private List<Future<Boolean>> queueTests() {
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        List<VisualStudioProject> testProjects = microsoftWindowsEnvironment.getTestProjects(pattern);
        List<Future<Boolean>> results = new ArrayList<>();
        for(VisualStudioProject project:testProjects){
            String projectName=project.getAssemblyName();
            Callable<Boolean> callable = createTestRunner(projectName);
            results.add(executorService.submit(callable));
        }
        return results;
    }
    private Callable<Boolean> createTestRunner(String projectName) {
        IntegrationTestRunner testRunner = testRunnerFactory.create();
        File coverageFile = new File(fileSystem.workDir(),"coverage_" + projectName + ".xml");
        testRunner.setCoverageFile(coverageFile)
        .setProjectName(projectName)
        .setModule(projectName)
        .setCoverageRoot(integrationTestsConfiguration.getDirectory())
        .setTestCaseFilter(integrationTestsConfiguration.getTestCaseFilter());
        Callable<Boolean> callable= new CallableTestRunner(testRunner, projectName, testResultsMap);
        LOG.debug("Queued {}",projectName);
        return callable;
    }



}
