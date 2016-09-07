/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;


import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.housekeeping.OrphanedTestRunnerRemover;
import com.stevpet.sonar.plugins.dotnet.mscover.housekeeping.MwicBridge;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

/**
 * Starts a thread for each project, and waits for the completion of all tests
 * @author stevpet
 *
 */
public class MultiThreadedSpecflowIntegrationTestApplication  implements IntegrationTestRunnerApplication {
    private static final int MINUTES_TO_MILLIS = 60000;
    private static final int ONE_MINUTE = 60000;
    private static Logger LOG = LoggerFactory.getLogger(MultiThreadedSpecflowIntegrationTestApplication.class);
    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private final IntegrationTestsConfiguration integrationTestsConfiguration;
    private ExecutorService executorService;
    private final FileSystem fileSystem;
    private IntegrationTestRunnerFactory testRunnerFactory;
    private MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecFlowIntegrationTestCache;
    private final OrphanedTestRunnerRemover orphanedTestRunnerRemover;
    public MultiThreadedSpecflowIntegrationTestApplication(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            IntegrationTestsConfiguration integrationTestsConfiguration,
            IntegrationTestRunnerFactory testRunnerFactory,
            FileSystem fileSystem, 
            MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecflowIntegrationTestCache,
            OrphanedTestRunnerRemover orphanedTestRunnerRemover) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.integrationTestsConfiguration=integrationTestsConfiguration;
        this.testRunnerFactory=testRunnerFactory;
        this.fileSystem=fileSystem;
        this.multiThreadedSpecFlowIntegrationTestCache = multiThreadedSpecflowIntegrationTestCache;
        this.orphanedTestRunnerRemover = orphanedTestRunnerRemover;
    }

    /**
     * DI Constructor
     * @param microsoftWindowsEnvironment
     * @param integrationTestsConfiguration
     * @param msCoverConfiguration
     * @param fileSystem
     * @param settings
     * @param coverageHashes 
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
                multiThreadedSpecflowIntegrationTestCache,
                new OrphanedTestRunnerRemover(
                        new MwicBridge(
                                new WindowsCommandLineExecutor()
                                )
                        )
        );
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
        int timeoutMinutes = integrationTestsConfiguration.getTestRunnerTimeout();
        executorService = Executors.newFixedThreadPool(threads);
        LOG.debug("Using {} threads",threads);
        List<TestRunnerThreadValues> results = queueTests();
        waitTillDone(timeoutMinutes, results);
        orphanedTestRunnerRemover.execute();
        multiThreadedSpecFlowIntegrationTestCache.setDidExecute(true);
    }


    private void waitTillDone(int timeout, List<TestRunnerThreadValues> testRunnersThreadValues) {
        try {
            executorService.shutdown();
            if (!waitTillAllTasksAreCompletedOrTimedout(timeout,testRunnersThreadValues)) {
                String msg="Timeout occurred during execution of tests after " + timeout + " minutes";
                LOG.error(msg);
                for(TestRunnerThreadValues testRunnerThreadValues:testRunnersThreadValues) {
                    Future<Boolean> future=testRunnerThreadValues.getFuture();
                    if(!future.isDone()) {
                        LOG.error("Shutting down {}",testRunnerThreadValues.getName());
                        future.cancel(true);
                    }
                }
                throw new IllegalStateException(msg);
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
            throw new IllegalStateException("Execution of tests failed, see inner exception {}",e.getCause());
        }
    }

    private boolean waitTillAllTasksAreCompletedOrTimedout(int timeoutMinutes, List<TestRunnerThreadValues> testRunnersThreadValues) throws InterruptedException {
        long start = System.currentTimeMillis();
        while(!allTasksCompleted(testRunnersThreadValues)) {
            Thread.sleep(ONE_MINUTE);
            if(timedout(start,timeoutMinutes*MINUTES_TO_MILLIS)) {
                return false;
            }
        }
        return true;
        
    }

    /**
     * 
     * @param startMillis - beginning of action, in millis
     * @param timeoutMillis - timeout, in millis
     * @return
     */
    private boolean timedout(long startMillis, int timeoutMillis) {
        long currentMillis = System.currentTimeMillis();
        return (currentMillis - startMillis) > timeoutMillis ;
    }

    private boolean allTasksCompleted(List<TestRunnerThreadValues> testRunnersThreadValues) {
        for(TestRunnerThreadValues testRunnerThreadValues:testRunnersThreadValues) {
            Future<Boolean> future=testRunnerThreadValues.getFuture();
            if(!future.isDone()) {
                return false;
            }
        }
        return true;
    }
    
    private List<TestRunnerThreadValues> queueTests() {
        List<TestRunnerThreadValues> results = new ArrayList<>();
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        List<VisualStudioProject> testProjects = microsoftWindowsEnvironment.getTestProjects(pattern);
        if(testProjects.size()==0) {
            LOG.warn("No integrationtest projects found with pattern {}",pattern);
            return results;
        }
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
        .setRetries(integrationTestsConfiguration.getTestRunnerRetries())
        .setTestCaseFilter(integrationTestsConfiguration.getTestCaseFilter());
        
        Callable<Boolean> callable= new CallableTestRunner(testRunner, projectName, multiThreadedSpecFlowIntegrationTestCache);
        LOG.debug("Queued {}",projectName);
        return callable;
    }



}
