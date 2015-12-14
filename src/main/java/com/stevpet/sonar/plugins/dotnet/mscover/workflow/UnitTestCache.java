package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface UnitTestCache {

	File getTestResults();

	File getTestCoverage();

	boolean hasRun();

	void setHasRun(File coverageFile, File testResultsFile);

	UnitTestCache setSonarCoverage(SonarCoverage sonarCoverage);

	SonarCoverage getSonarCoverage();

}