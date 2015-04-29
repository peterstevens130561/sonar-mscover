package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;


import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.NullCoverageReader;

public class NullCoverageSaver implements CoverageSaver {
	private final static Logger LOG = LoggerFactory.getLogger(NullCoverageSaver.class);
	@Override
	public void save(SonarCoverage sonarCoverage) {
		// As this is null class, no implementation
		LOG.info("Invoked");
	}

	@Override
	public void setExcludeSourceFiles(List<File> testFiles) {
		// As this is null class, no implementation		
	}

}
