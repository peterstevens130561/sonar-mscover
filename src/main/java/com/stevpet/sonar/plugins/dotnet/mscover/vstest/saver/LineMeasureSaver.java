package com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;

public interface LineMeasureSaver {

    public void saveMeasures(
            FileLineCoverage coverageData, File file);
}