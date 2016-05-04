package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;

class NullTestResultsBuilder implements TestResultsBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(NullTestResultsBuilder.class);
	@Override
	public ProjectUnitTestResults parse(File testResultsFile, File coverageFile) {
		return new ProjectUnitTestResults();
	}

}
