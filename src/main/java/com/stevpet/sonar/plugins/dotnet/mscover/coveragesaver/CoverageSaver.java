package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;


import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface CoverageSaver {
	/**
	 * Save the coverage data
	 * @param project
	 * @param context
	 * @param coverage - completed coverage data
	 */
	void save(SonarCoverage sonarCoverage);
}
