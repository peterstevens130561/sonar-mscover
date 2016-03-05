package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.DefaultIntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpecFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.IntegrationTestCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

/**
 * A testrunner which keeps its own cache
 * 
 * @author stevpet
 * 
 */
public class SpecflowIntegrationTestRunner implements
		IntegrationTestRunner {

	private Logger LOG = LoggerFactory.getLogger(SpecflowIntegrationTestRunner.class);
	private final IntegrationTestCache integrationTestCache;
	private final OpenCoverModuleSaver openCoverModuleSaver;
	private final OpenCoverTestRunner testRunner;
	private final TestResultsBuilder testResultsBuilder;
	private String module;
	private File rootDir;
	private File coverageFile;
	private ProjectUnitTestResults testResults;
    private String projectName;
    private String testCaseFilter;

	/**
	 * All of the dependencies of the runner
	 * 
	 * @param integrationTestCache
	 * @param openCoverModuleSaver
	 * @param testRunner
	 */
	public SpecflowIntegrationTestRunner(
			IntegrationTestCache integrationTestCache,
			OpenCoverModuleSaver openCoverModuleSaver, OpenCoverTestRunner testRunner,
			TestResultsBuilder testResultsBuilder) {
		this.integrationTestCache = integrationTestCache;
		this.openCoverModuleSaver = openCoverModuleSaver;
		this.testRunner = testRunner;
		this.testResultsBuilder = testResultsBuilder;

	}

	/**
	 * Creates an instance which is to be used in the compositionroot of the
	 * sensor
	 * 
	 * @param integrationTestCache
	 * @param msCoverConfiguration
	 * @param microsoftWindowsEnvironment
	 * @param fileSystem
	 * @param vsTestEnvironment
	 * @return
	 */
	public static SpecflowIntegrationTestRunner create(
			IntegrationTestCache integrationTestCache,
			MsCoverConfiguration msCoverConfiguration,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			FileSystem fileSystem, VsTestEnvironment vsTestEnvironment,Settings settings) {
		return new SpecflowIntegrationTestRunner(integrationTestCache,
				new OpenCoverModuleSaver(), DefaultOpenCoverTestRunner.create(
						msCoverConfiguration, microsoftWindowsEnvironment,
						fileSystem, vsTestEnvironment),
				SpecFlowTestResultsBuilder.create(microsoftWindowsEnvironment));
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.
	 * IntegrationTestRunner#setModule(java.lang.String)
	 */
	@Override
	public IntegrationTestRunner setModule(@Nonnull String module) {
		this.module = module;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.
	 * IntegrationTestRunner#setCoverageRoot(java.io.File)
	 */
	@Override
	public IntegrationTestRunner setCoverageRoot(File rootDir) {
		this.rootDir = rootDir;
		return this;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner.
	 * IntegrationTestRunner#setCoverageFile(java.io.File)
	 */
	@Override
	public IntegrationTestRunner setCoverageFile(
			@Nonnull File coverageFile) {
		this.coverageFile = coverageFile;
		return this;
	}

	@Override
	public void execute() {
		Preconditions.checkNotNull(module,"module not set");
		Preconditions.checkNotNull(coverageFile,"coverage file not set");
		Preconditions.checkNotNull(rootDir,"rootDir not set");

		openCoverModuleSaver.setProject(module).setRoot(rootDir);
        Pattern pattern=Pattern.compile(projectName);
		testRunner.setTestProjectPattern(pattern);
		
		testRunner.setCoverageFile(coverageFile);
		
		testRunner.setTestCaseFilter(testCaseFilter);
		testRunner.execute();
		
		File testResultsFile = testRunner.getTestResultsFile();
		integrationTestCache.setHasRun(coverageFile, testResultsFile);
		openCoverModuleSaver.splitFile(coverageFile);
		File moduleCoverageFile = openCoverModuleSaver.getCoverageFile(module);
		if(!moduleCoverageFile.exists()) {
			String msg="Can't find coverage file for project '" + module + "'\n most likely cause is that the sonar.visualstudio.testProjectPattern property is incorrect";
			Log.error(msg);
			throw new SonarException(msg);
		}
		testResults = testResultsFile == null ? new ProjectUnitTestResults()
				: testResultsBuilder.parse(testResultsFile, moduleCoverageFile);
		integrationTestCache.setTestResults(testResults);
	}


	@Override
	public ProjectUnitTestResults getTestResults() {
		return integrationTestCache.getTestResults();
	}

    @Override
    public IntegrationTestRunner setProjectName(String name) {
        this.projectName=name;
        return this;
    }

    @Override
    public IntegrationTestRunner setTestCaseFilter(String testCaseFilter) {
        this.testCaseFilter=testCaseFilter;
        return this;
    }
}
