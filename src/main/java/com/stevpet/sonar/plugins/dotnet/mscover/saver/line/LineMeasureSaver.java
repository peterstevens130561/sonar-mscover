package com.stevpet.sonar.plugins.dotnet.mscover.saver.line;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;

public interface LineMeasureSaver {

    public void saveMeasures(
            FileCoverage coverageData, File file);
}