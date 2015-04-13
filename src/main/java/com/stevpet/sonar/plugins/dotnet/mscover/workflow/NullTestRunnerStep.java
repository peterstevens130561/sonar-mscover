package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.apache.commons.lang.StringUtils;

public class NullTestRunnerStep implements TestRunnerStep {

	@Override
	public void execute() {
	}


	@Override
	public File getTestResultsFile() {
		return null;
	}

}
