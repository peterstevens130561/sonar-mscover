package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchComponent;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.housekeeping.OrphanedTestRemoverThread;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor.LogChanger;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

/**
 * Starts a thread for each project, and waits for the completion of all tests
 * @author stevpet
 *
 */
public class MultiThreadedSpecflowIntegrationTestApplication  implements IntegrationTestRunnerApplication {
    private static Logger LOG = LoggerFactory.getLogger(MultiThreadedSpecflowIntegrationTestApplication.class);
    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private final IntegrationTestsConfiguration integrationTestsConfiguration;
    private ExecutorService executorService;
    private final FileSystem fileSystem;
    private IntegrationTestRunnerFactory testRunnerFactory;
    private MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecFlowIntegrationTestCache;
    public MultiThreadedSpecflowIntegrationTestApplication(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            IntegrationTestsConfiguration integrationTestsConfiguration,
            IntegrationTestRunnerFactory testRunnerFactory,
            FileSystem fileSystem, MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecflowIntegrationTestCache) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.integrationTestsConfiguration=integrationTestsConfiguration;
        this.testRunnerFactory=testRunnerFactory;
        this.fileSystem=fileSystem;
        this.multiThreadedSpecFlowIntegrationTestCache = multiThreadedSpecflowIntegrationTestCache;
    }

    /**
     * DI Constructor
     * @param microsoftWindowsEnvironment
     * @param integrationTestsConfiguration
     * @param msCoverConfiguration
     * @param fileSystem
     * @param settings
     */
    public MultiThreadedSpecflowIntegrationTestApplication(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            IntegrationTestsConfiguration integrationTestsConfiguration,
            MsCoverConfiguration msCoverConfiguration,
            FileSystem fileSystem,
            Settings settings,
            MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecflowIntegrationTestCache) {
        this(microsoftWindowsEnvironment,
                integrationTestsConfiguration,
                new DefaultIntegrationTestRunnerFactory(
                        msCoverConfiguration, settings,
                        microsoftWindowsEnvironment,
                        fileSystem
                ),
                fileSystem, 
                multiThreadedSpecflowIntegrationTestCache);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.IntegrationTestRunnerApplication#getTestResults()
     */
    @Override
    public ProjectUnitTestResults getTestResults(String module) {
            LOG.debug("Getting results for {}",module);
            return multiThreadedSpecFlowIntegrationTestCache.getTestResults(module);
  
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.IntegrationTestRunnerApplication#execute()
     */
    @Override
    public void execute() {
        if(multiThreadedSpecFlowIntegrationTestCache.getDidExecute()) {
            LOG.debug("Using previous data");
            return;
        }
        int threads = integrationTestsConfiguration.getTestRunnerThreads();
        int timeout = integrationTestsConfiguration.getTestRunnerTimeout();
        executorService = Executors.newFixedThreadPool(threads);
        LOG.debug("Using {} threads",threads);
        List<TestRunnerThreadValues> results = queueTests();
        OrphanedTestRemoverThread cleaner = new OrphanedTestRemoverThread();
        Thread cleanerThread = new Thread(cleaner);
        cleanerThread.start();
        waitTillDone(timeout, results);
        //cleanerThread.interrupt();
        cleaner.stop();
        
        multiThreadedSpecFlowIntegrationTestCache.setDidExecute(true);
    }


    private void waitTillDone(int timeout, List<TestRunnerThreadValues> testRunnersThreadValues) {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(timeout, TimeUnit.MINUTES)) {
                String msg="Timeout occurred during execution of tests after " + timeout + " minutes";
                LOG.error(msg);
                for(TestRunnerThreadValues testRunnerThreadValues:testRunnersThreadValues) {
                    Future<Boolean> future=testRunnerThreadValues.getFuture();
                    if(!future.isDone()) {
                        LOG.error("Shutting down {}",testRunnerThreadValues.getName());
                        future.cancel(true);
                    }
                }
                throw new SonarException(msg);
            }
            for(TestRunnerThreadValues testRunnerThreadValues:testRunnersThreadValues) {
                Future<Boolean> future=testRunnerThreadValues.getFuture();
                LOG.info("Getting result from {}",testRunnerThreadValues.getName());
                future.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            LOG.error("Execution of tests failed {}",e.getCause().toString());
            throw new SonarException("Execution of tests failed, see inner exception",e.getCause());
        }
    }
    
    private List<TestRunnerThreadValues> queueTests() {
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        List<VisualStudioProject> testProjects = microsoftWindowsEnvironment.getTestProjects(pattern);
        List<TestRunnerThreadValues> results = new ArrayList<>();
        for(VisualStudioProject project:testProjects){
            String projectName=project.getAssemblyName();
            Callable<Boolean> callable = createTestRunner(projectName);
            Future<Boolean> future=executorService.submit(callable);
            TestRunnerThreadValues threadValues = new TestRunnerThreadValues(future,callable,projectName);
            results.add(threadValues);
        }
        return results;
    }
    private Callable<Boolean> createTestRunner(String projectName) {
        IntegrationTestRunner testRunner = testRunnerFactory.create();
        File coverageFile = new File(fileSystem.workDir(),"coverage_" + projectName + ".xml");
        testRunner.setCoverageFile(coverageFile)
        .setTimeout(120)
        .setProjectName(projectName)
        .setModule(projectName)
        .setCoverageRoot(integrationTestsConfiguration.getDirectory())
        .setTestCaseFilter(integrationTestsConfiguration.getTestCaseFilter());
        Callable<Boolean> callable= new CallableTestRunner(testRunner, projectName, multiThreadedSpecFlowIntegrationTestCache);
        LOG.debug("Queued {}",projectName);
        return callable;
    }



}
