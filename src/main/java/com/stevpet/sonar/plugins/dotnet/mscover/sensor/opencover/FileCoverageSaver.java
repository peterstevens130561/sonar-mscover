package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;

public interface FileCoverageSaver {

    public abstract void saveMeasures(CoverageLinePoints coveragePoints,
            File file);

}