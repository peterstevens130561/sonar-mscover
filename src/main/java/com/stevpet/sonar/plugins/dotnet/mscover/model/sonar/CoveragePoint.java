package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

public interface CoveragePoint {

    abstract int getToCover();

    abstract int getCovered();    

}