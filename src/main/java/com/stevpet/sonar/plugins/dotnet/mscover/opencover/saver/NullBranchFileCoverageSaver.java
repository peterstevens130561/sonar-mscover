package com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public class NullBranchFileCoverageSaver implements BranchFileCoverageSaver {

	@Override
	public void saveMeasures(CoverageLinePoints coveragePoints, File file) {
		// Intentionally does nothing
	}

}
