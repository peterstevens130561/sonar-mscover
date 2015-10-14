package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;

public class SpecFlowScenarioMap {
    private Map<String,SpecFlowScenario> map = new HashMap<>();
    
    public void put(MethodId testMethod) {
        SpecFlowScenario scenario = testMethod2Scenario(testMethod);
        String key=getKey(scenario);
        map.put(key,scenario);
    }
    public SpecFlowScenario testMethod2Scenario(MethodId testMethod) {
        SpecFlowScenario scenario = new SpecFlowScenario();
        scenario.setMethodName(testMethod.getMethodName());
        scenario.setNamespace(testMethod.getNamespaceName());
        return scenario;
    }
    public void put(SpecFlowScenario scenario) {
        String key=getKey(scenario);
        map.put(key,scenario);
    }
    
    private String getKey(SpecFlowScenario scenario) {

        return getKey(scenario.getNamespace(),scenario.getMethodName());
    }

    public void putAll(SpecFlowScenarioMap otherMap) {
        map.putAll(otherMap.map);
    }
    
    public SpecFlowScenario get(MethodId testMethod) {
        SpecFlowScenario scenario = testMethod2Scenario(testMethod);
        return get(scenario.getNamespace(),scenario.getMethodName());
    }
    /**
     * return the scenario that has this methodName. If there is no file, then a scenario is returned with null file
     * @param methodName
     * @return SpecFlowScenario
     */
    public SpecFlowScenario get(String namespace, String methodName) {
        String key=getKey(namespace, methodName);
        SpecFlowScenario scenario= map.get(key);
        if(scenario==null) {
            scenario= new SpecFlowScenario(null,namespace,methodName,methodName);
        }
        return scenario;
    }

    public String getKey(String namespace, String methodName) {
        return namespace + ":" + methodName;
    }
    
    public int size() {
        return map.size();
    }



}
