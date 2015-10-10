package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SpecFlowScenarioMap {
    private Map<String,SpecFlowScenario> map = new HashMap<>();
    
    public void put(SpecFlowScenario scenario) {
        
        map.put(scenario.getMethodName(),scenario);
    }
    
    public void putAll(SpecFlowScenarioMap otherMap) {
        map.putAll(otherMap.map);
    }
    
    /**
     * return the scenario that has this methodName. If there is no file, then a scenario is returned with null file
     * @param methodName
     * @return SpecFlowScenario
     */
    public SpecFlowScenario get(String methodName) {
        SpecFlowScenario scenario= map.get(methodName);
        if(scenario==null) {
            scenario= new SpecFlowScenario(null,methodName,methodName);
        }
        return scenario;
    }

}
