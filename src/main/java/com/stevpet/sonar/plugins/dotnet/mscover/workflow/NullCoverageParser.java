package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class NullCoverageParser implements CoverageParserStep {
	private final static Logger LOG = LoggerFactory.getLogger(NullCoverageParser.class);
	
	@Override
	public void parse(SonarCoverage registry, File file) {
		LOG.info("Invoked");
	}

}
