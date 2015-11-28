package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver;

import java.io.File;

import org.sonar.api.batch.SensorContext;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public class NullBranchFileCoverageSaver implements BranchFileCoverageSaver {

	@Override
	public void saveMeasures(CoverageLinePoints coveragePoints, File file) {
		// Intentionally does nothing
	}

	@Override
	public void setSensorContext(SensorContext sensorContext) {
		// TODO Auto-generated method stub
		
	}

}
