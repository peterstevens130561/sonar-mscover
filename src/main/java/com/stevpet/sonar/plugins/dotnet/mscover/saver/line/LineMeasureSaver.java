package com.stevpet.sonar.plugins.dotnet.mscover.saver.line;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;

public interface LineMeasureSaver {

    public void saveMeasures(
            FileCoverage coverageData, File file);
}