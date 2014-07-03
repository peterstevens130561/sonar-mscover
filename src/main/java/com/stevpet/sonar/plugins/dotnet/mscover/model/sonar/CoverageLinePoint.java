package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

public interface CoverageLinePoint extends CoveragePoint {
    abstract int getLine();
    
    abstract void setLine(int line);
}
