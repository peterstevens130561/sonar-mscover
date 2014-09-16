package com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;

public interface LineMeasureSaver {

    public void saveMeasures(
            FileCoverage coverageData, File file);
}