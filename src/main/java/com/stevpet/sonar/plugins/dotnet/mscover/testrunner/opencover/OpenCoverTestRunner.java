package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

public interface OpenCoverTestRunner extends TestRunner {


	
	/**
	 * only the assemblies of this solution will be reported in the coverage file
	 * @return 
	 */
	OpenCoverTestRunner onlyReportAssembliesOfTheSolution();

	void setTestCaseFilter(String testCaseFilter);


}
