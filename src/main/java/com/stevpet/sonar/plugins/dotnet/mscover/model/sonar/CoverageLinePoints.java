package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import java.util.List;

public interface CoverageLinePoints {

    abstract CoveragePoint getLast();

    abstract int size();

    abstract SonarCoverageSummary getSummary();
    
    abstract CoveragePoint addPoint(int line, boolean visited);

    abstract List<CoverageLinePoint> getPoints();

}