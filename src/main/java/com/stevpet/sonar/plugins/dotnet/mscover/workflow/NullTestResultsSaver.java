package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullTestResultsSaver implements TestResultsSaver {
	private final static Logger LOG = LoggerFactory.getLogger(NullTestResultsSaver.class);
	
	@Override
	public void save(UnitTestResults unitTestResults) {
		LOG.info("Invoked");
	}

}
