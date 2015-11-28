package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.nullsaver;

import java.io.File;

import org.sonar.api.batch.SensorContext;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public class NullLineFileCoverageSaver implements LineFileCoverageSaver {

	@Override
	public void saveMeasures(CoverageLinePoints coveragePoints, File file) {
		// As this is the null object it does nothing

	}

	@Override
	public void setSensorContext(SensorContext sensorContext) {
		// TODO Auto-generated method stub
		
	}

}
