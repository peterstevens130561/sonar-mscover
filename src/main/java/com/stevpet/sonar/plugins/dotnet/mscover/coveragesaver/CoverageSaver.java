package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;


import org.sonar.api.batch.SensorContext;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface CoverageSaver  {
	/**
	 * Save the coverage data
	 * @param project
	 * @param context
	 * @param coverage - completed coverage data
	 */
	@Deprecated
	void save(SonarCoverage sonarCoverage);

	void save(SensorContext sensorContext,SonarCoverage sonarCoverage);

}
