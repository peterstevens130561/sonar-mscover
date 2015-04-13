package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class NullCoverageParser implements CoverageParserStep {

	@Override
	public void parse(SonarCoverage registry, File file) {
	}

}
