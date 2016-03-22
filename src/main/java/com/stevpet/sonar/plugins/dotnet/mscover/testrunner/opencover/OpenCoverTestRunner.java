package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

public interface OpenCoverTestRunner extends TestRunner {

	/**
	 * only the assemblies of this solution will be reported in the coverage file
	 * @return 
	 */
	OpenCoverTestRunner onlyReportAssembliesOfTheSolution();

	void setTestCaseFilter(String testCaseFilter);

	/**
	 * only test projects that match this pattern should be included in the test run.
	 * @param string
	 */
    void setTestProjectPattern(@Nonnull Pattern string);

    /**
     * set timeout in minutes
     * @param timeout
     */
    void setTimeout(int timeout);


}
