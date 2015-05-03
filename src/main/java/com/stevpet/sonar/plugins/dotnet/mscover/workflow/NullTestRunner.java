package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
