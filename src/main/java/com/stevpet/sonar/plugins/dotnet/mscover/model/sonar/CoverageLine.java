package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

public interface CoverageLine {
    
    abstract int getLine();
    
    abstract void setLine(int line);
}
