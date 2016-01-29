package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface TestCache {

	File getTestResultsFile();

	File getTestCoverageFile();

	boolean gatHasRun();

	void setHasRun(File coverageFile, File testResultsFile);

	TestCache setSonarCoverage(SonarCoverage sonarCoverage);

	SonarCoverage getSonarCoverage();

	void setHasRun(boolean b);

}