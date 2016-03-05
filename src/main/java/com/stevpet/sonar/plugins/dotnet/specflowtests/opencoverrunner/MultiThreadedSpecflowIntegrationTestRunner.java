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
import java.util.concurrent.FutureTask;
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
public class MultiThreadedSpecflowIntegrationTestRunner implements CachedIntegrationTestRunner {
    private static Logger LOG = LoggerFactory.getLogger(MultiThreadedSpecflowIntegrationTestRunner.class);
    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private final IntegrationTestsConfiguration integrationTestsConfiguration;
    private ExecutorService executorService;
    private boolean didExecute;
    // lookup testresults by module
    private  Map<String,ProjectUnitTestResults> testResultsMap = new HashMap<>();
    private String module;
    private File rootDir;
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
    @Override
    public CachedIntegrationTestRunner setModule(String module) {
        this.module=module;
        return this;
    }

    @Override
    public CachedIntegrationTestRunner setCoverageRoot(File rootDir) {
        this.rootDir=rootDir;
        return this;
    }

    @Override
    public CachedIntegrationTestRunner setCoverageFile(File coverageFile) {
        return this;
    }

    @Override
    public ProjectUnitTestResults getTestResults() {
        synchronized(testResultsMap) {
            LOG.info("+++ Getting results for {}",module);
            return testResultsMap.get(module);
        }
    }

    @Override
    public void execute() {
        if(didExecute) {
            LOG.info("+++ Using previous data");
            return;
        }

        int threads = integrationTestsConfiguration.getCoverageReaderThreads();
        executorService = Executors.newFixedThreadPool(threads);
        LOG.info("Using {} threads",threads);
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        List<VisualStudioProject> testProjects = microsoftWindowsEnvironment.getTestProjects(pattern);
        List<Future<Boolean>> results = new ArrayList<>();
        for(VisualStudioProject project:testProjects){
            String projectName=project.getAssemblyName();
            CachedIntegrationTestRunner testRunner = testRunnerFactory.create();
            File coverageFile = new File(fileSystem.workDir(),"coverage_" + projectName + ".xml");
            testRunner.setCoverageFile(coverageFile).setProjectName(projectName).setModule(projectName);
            Callable<Boolean> callable= new CallableTestRunner(testRunner, projectName, testResultsMap);
            LOG.info("+++ Queued {}",projectName);
            results.add(executorService.submit(callable));
 
        }
        try {
            executorService.shutdown();
            int timeout = integrationTestsConfiguration.getCoverageReaderTimeout();
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
        didExecute=true;
    }


    @Override
    public CachedIntegrationTestRunner setProjectName(String name) {
        // TODO Auto-generated method stub
        return this;
    }
    

}
