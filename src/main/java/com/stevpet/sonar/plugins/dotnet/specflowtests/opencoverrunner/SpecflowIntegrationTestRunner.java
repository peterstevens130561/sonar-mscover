package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.OpenCoverModuleSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpecFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverTestRunner;
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
	private final OpenCoverModuleSaver openCoverModuleSaver;
	private final OpenCoverTestRunner testRunner;
	private final TestResultsBuilder testResultsBuilder;
	private String module;
	private File rootDir;
	private File coverageFile;
	private ProjectUnitTestResults testResults;
    private String projectName;
    private String testCaseFilter;
    private int timeout ;

	/**
	 * All of the dependencies of the runner
	 * 
	 * @param integrationTestCache
	 * @param openCoverModuleSaver
	 * @param testRunner
	 */
	public SpecflowIntegrationTestRunner(
			OpenCoverModuleSaver openCoverModuleSaver, OpenCoverTestRunner testRunner,
			TestResultsBuilder testResultsBuilder) {
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
			MsCoverConfiguration msCoverConfiguration,
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			FileSystem fileSystem,Settings settings) {
		return new SpecflowIntegrationTestRunner(
				new OpenCoverModuleSaver(), DefaultOpenCoverTestRunner.create(
						msCoverConfiguration, microsoftWindowsEnvironment,
						fileSystem),
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
		Preconditions.checkState(timeout>0,"timeout invalid,, should be > 0");

		openCoverModuleSaver.setProject(module).setRoot(rootDir);
        Pattern pattern=Pattern.compile(projectName);
		testRunner.setTestProjectPattern(pattern);
		testRunner.setTimeout(timeout);
		testRunner.setCoverageFile(coverageFile);
		
		testRunner.setTestCaseFilter(testCaseFilter);
		testRunner.execute();
		
		File testResultsFile = testRunner.getTestResultsFile();
		openCoverModuleSaver.splitFile(coverageFile);
		coverageFile.delete();

		File moduleCoverageFile = openCoverModuleSaver.getCoverageFile(module);
        LOG.info("!!! module {} path {}",module,moduleCoverageFile.getAbsolutePath());
		if(!moduleCoverageFile.exists()) {
			String msg="Can't find coverage file '" + moduleCoverageFile.getAbsolutePath() + "'  for project '" + module + "'\n most likely cause is that the sonar.visualstudio.testProjectPattern property is incorrect";
			LOG.error(msg);
			throw new SonarException(msg);
		}
		testResults = testResultsFile == null ? new ProjectUnitTestResults()
				: testResultsBuilder.parse(testResultsFile, moduleCoverageFile);
	}


	@Override
	public ProjectUnitTestResults getTestResults() {
		return testResults;
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

    @Override
    public IntegrationTestRunner setTimeout(int timeout) {
        this.timeout=timeout;
        return this;
    }
}
