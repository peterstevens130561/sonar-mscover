package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullTestResultsBuilder implements TestResultsBuilder {
	private final static Logger LOG = LoggerFactory.getLogger(NullTestResultsBuilder.class);
	@Override
	public UnitTestResults parse(File testResultsFile, File coverageFile) {
		LOG.info("Invoked");
		return new UnitTestResults();
	}

}
