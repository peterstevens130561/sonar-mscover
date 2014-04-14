package com.stevpet.sonar.plugins.dotnet.mscover.registry;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jfree.util.Log;

import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestResultModel;
public class UnitTestResultRegistry {
    Map<String,UnitTestResultModel> unitTestResultsById = new HashMap<String,UnitTestResultModel>();
    
    public void add(UnitTestResultModel unitTestResult) {
        String testId=unitTestResult.getTestId();
        if(unitTestResultsById.containsKey(testId)) {
            Log.warn("UnitTestResult for test already stored :" + testId);
        }
        unitTestResultsById.put(testId,unitTestResult);
    }
    
    /**
     * gets the testResult identified by the name
     * @param testName
     * @return 
     */
    public UnitTestResultModel getById(String testName) {
        return unitTestResultsById.get(testName);
    }

    public int size() {
        return unitTestResultsById.size();
    }
    
    public Collection<UnitTestResultModel> values() {
        return unitTestResultsById.values();
    }
}
