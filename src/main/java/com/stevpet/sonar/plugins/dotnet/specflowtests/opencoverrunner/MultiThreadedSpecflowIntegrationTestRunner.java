package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private final IntegrationTestsConfiguration integrationTestsConfiguration;
    private final CachedIntegrationTestRunner testRunner;
    private ExecutorService executorService;
    private boolean didExecute;
    // lookup testresults by module
    private Map<String,ProjectUnitTestResults> testResultsMap = new HashMap<>();
    private String module;
    private File rootDir;
    private final FileSystem fileSystem;
    public MultiThreadedSpecflowIntegrationTestRunner(MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            IntegrationTestsConfiguration integrationTestsConfiguration,
            CachedIntegrationTestRunner testRunner,
            FileSystem fileSystem) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.integrationTestsConfiguration=integrationTestsConfiguration;
        this.testRunner=testRunner;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProjectUnitTestResults getTestResults() {
        synchronized(testResultsMap) {
            return testResultsMap.get(module);
        }
    }

    @Override
    public void execute() {
        if(didExecute) {
            return;
        }
        Pattern pattern=integrationTestsConfiguration.getTestProjectPattern();
        int threads = integrationTestsConfiguration.getCoverageReaderThreads();
        executorService = Executors.newFixedThreadPool(threads);
        List<VisualStudioProject> testProjects = microsoftWindowsEnvironment.getCurrentSolution().getTestProjects(pattern);
        for(VisualStudioProject project:testProjects){
            runTests(project);
        }
        try {
            executorService.shutdown();
            int timeout = integrationTestsConfiguration.getCoverageReaderTimeout();
            if (!executorService.awaitTermination(timeout, TimeUnit.MINUTES)) {
                throw new SonarException("Timeout occurred during parsing of coveragefiles");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        didExecute=true;
    }

    private void runTests(VisualStudioProject project) {
        String projectName = project.getAssemblyName();
        File coverageFile = new File(fileSystem.workDir(),"coverage_" + projectName);
        testRunner.setCoverageFile(coverageFile).setProjectName(projectName).setModule(projectName);
        testRunner.execute();
        ProjectUnitTestResults testResults = testRunner.getTestResults();
        setTestResults(projectName,testResults);
    }
    
    private void setTestResults(String projectName,ProjectUnitTestResults testResults) {
        synchronized(testResultsMap) {
            testResultsMap.put(projectName,testResults);
        }
    }
    @Override
    public CachedIntegrationTestRunner setProjectName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
