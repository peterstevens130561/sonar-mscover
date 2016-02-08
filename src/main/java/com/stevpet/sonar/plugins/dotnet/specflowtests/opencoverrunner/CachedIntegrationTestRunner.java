package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;

public interface CachedIntegrationTestRunner {

	CachedIntegrationTestRunner setModule(String module);

	CachedIntegrationTestRunner setCoverageRoot(File rootDir);

	CachedIntegrationTestRunner setCoverageFile(File coverageFile);

	/**
	 * get the test results of the integration tests in this solution
	 * @return
	 */
	ProjectUnitTestResults getTestResults();

	void execute();
}