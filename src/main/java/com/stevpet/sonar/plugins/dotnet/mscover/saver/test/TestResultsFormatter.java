package com.stevpet.sonar.plugins.dotnet.mscover.saver.test;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;

public interface TestResultsFormatter extends BatchExtension {

    String formatClassUnitTestResults(ClassUnitTestResult fileResults);

}