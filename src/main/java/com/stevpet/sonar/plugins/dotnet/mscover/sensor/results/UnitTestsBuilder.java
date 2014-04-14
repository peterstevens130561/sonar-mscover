package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.UnitTestResultRegistry;

public class UnitTestsBuilder {
    private UnitTestResultRegistry unitTestRegistry ;
    
    public void execute() {
        for(UnitTestResultModel test: unitTestRegistry.values()) {
        }
    }
}
