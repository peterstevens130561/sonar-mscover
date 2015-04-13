package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface CoverageSaver {
	/**
	 * Save the coverage data
	 * @param project
	 * @param context
	 * @param coverage - completed coverage data
	 */
	void save(Project project, SensorContext sonarContext,SonarCoverage sonarCoverage);
}
