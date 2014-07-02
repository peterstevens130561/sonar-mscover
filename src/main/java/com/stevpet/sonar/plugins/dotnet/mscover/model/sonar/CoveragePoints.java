package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

public interface CoveragePoints {

    abstract CoveragePoint getLast();

    abstract int size();

    abstract SonarCoverageSummary getSummary();
    
    abstract CoveragePoint addPoint(int line, boolean visited);

}