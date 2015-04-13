package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestRegistry;

public interface TestResultsBuilder {
	/**
	 * given the coverage file and the test results file, build a repository of test results
	 * @param valid testResultsFile
	 * @return
	 */
	UnitTestResults parse(File testResultsFile, File coverageFile);
}
