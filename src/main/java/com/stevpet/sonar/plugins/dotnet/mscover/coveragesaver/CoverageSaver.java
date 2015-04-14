package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;


import java.io.File;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public interface CoverageSaver {
	/**
	 * Save the coverage data
	 * @param project
	 * @param context
	 * @param coverage - completed coverage data
	 */
	void save(SonarCoverage sonarCoverage);

	void setExcludeSourceFiles(List<File> testFiles);
}
