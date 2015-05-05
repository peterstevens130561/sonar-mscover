package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class NullCoverageReader implements CoverageReader {
	private final static Logger LOG = LoggerFactory.getLogger(NullCoverageReader.class);
	
	@Override
	public void read(SonarCoverage registry, File file) {
		LOG.info("Invoked");
	}

}
