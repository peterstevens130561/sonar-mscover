package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class IntegrationTestStates {

    public Map<String,IntegrationTestState> states = new HashMap<>();
    
    public void add(@Nonnull String projectName) {
        states.put(projectName,new IntegrationTestState());
    }
    
    public IntegrationTestState getState(@Nonnull String projectName) {
        return states.get(projectName);
    }
}
