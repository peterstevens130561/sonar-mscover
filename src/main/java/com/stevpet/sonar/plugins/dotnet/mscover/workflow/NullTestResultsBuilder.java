package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

public class NullTestResultsBuilder implements TestResultsBuilder {

	@Override
	public UnitTestResults parse(File testResultsFile, File coverageFile) {
		return new UnitTestResults();
	}

}
