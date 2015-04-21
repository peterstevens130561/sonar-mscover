package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;

public interface TestResultsFormatter {

	String formatClassUnitTestResults(ClassUnitTestResult fileResults);

}