package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;


import java.io.File;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class NullCoverageSaver implements CoverageSaver {

	@Override
	public void save(SonarCoverage sonarCoverage) {
		// As this is null class, no implementation
	}

	@Override
	public void setExcludeSourceFiles(List<File> testFiles) {
		// As this is null class, no implementation		
	}

}
