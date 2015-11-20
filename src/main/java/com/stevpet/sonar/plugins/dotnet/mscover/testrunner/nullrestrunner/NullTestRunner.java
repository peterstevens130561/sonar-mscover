package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.nullrestrunner;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

public class NullTestRunner implements TestRunner {

	private final static Logger LOG = LoggerFactory.getLogger(NullTestRunner.class);
	
	@Override
	public void execute() {
		LOG.info("Invoked");
	}


	@Override
	public File getTestResultsFile() {
		return null;
	}


	@Override
	public void setCoverageFile(File coverageFile) {
	}

}
