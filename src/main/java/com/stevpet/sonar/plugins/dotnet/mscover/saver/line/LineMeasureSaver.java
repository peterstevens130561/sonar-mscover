package com.stevpet.sonar.plugins.dotnet.mscover.saver.line;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;

public interface LineMeasureSaver {

    public abstract void saveSummaryMeasures(SensorContext context,
            FileCoverage coverageData, Resource resource);

    public abstract Measure getHitData(FileCoverage fileCoverage);
}