package com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public class NullLineFileCoverageSaver implements LineFileCoverageSaver {

	@Override
	public void saveMeasures(CoverageLinePoints coveragePoints, File file) {
		// As this is the null object it does nothing

	}

}
