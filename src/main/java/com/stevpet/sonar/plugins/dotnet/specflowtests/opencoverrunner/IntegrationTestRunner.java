package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public interface IntegrationTestRunner {

	IntegrationTestRunner setModule(String module);

	IntegrationTestRunner setCoverageRoot(File rootDir);

	IntegrationTestRunner setCoverageFile(File coverageFile);

	/**
	 * get the test results of the integration tests in this solution
	 * @return
	 */
	ProjectUnitTestResults getTestResults();

	void execute();

	IntegrationTestRunner setProjectName(String name);

    IntegrationTestRunner setTestCaseFilter(String testCaseFilter);

    /**
     * 
     * @param timeout in minutes of the test runner
     * @return
     */
    IntegrationTestRunner setTimeout(int timeout);

    /**
     * @param retries number of retries to perform
     * @return
     */
    IntegrationTestRunner setRetries(int retries);
}