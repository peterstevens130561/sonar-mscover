package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class NullCoverageSaver implements CoverageSaver {
	private final static Logger LOG = LoggerFactory.getLogger(NullCoverageSaver.class);
	@Override
	public void save(SonarCoverage sonarCoverage) {
		// As this is null class, no implementation
		LOG.info("Invoked");
	}
	@Override
	public void save(SensorContext sensorContext, SonarCoverage sonarCoverage) {

	}


}
