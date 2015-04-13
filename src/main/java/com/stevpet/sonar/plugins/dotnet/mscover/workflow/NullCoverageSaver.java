package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class NullCoverageSaver implements CoverageSaver {

	@Override
	public void save(Project project, SensorContext sonarContext,
			SonarCoverage sonarCoverage) {
	}

}
