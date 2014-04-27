package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.util.ArrayList;
import java.util.List;

public class UnitTestFileResultModel {
    private int error ;
    
    private List<UnitTestResultModel> tests = new ArrayList<UnitTestResultModel>() ;
    
    public void add(UnitTestResultModel unitTest) {
        tests.add(unitTest);
        String outcome=unitTest.getOutcome();
        if(!"Passed".equals(outcome)) {
            error++;
        }
    }
    
    public double getPassed() {
        return (double) tests.size() - error;
    }
    
    public double getFail() {
        return (double) error ;
    }
    
    public double getTests() {
        return (double) tests.size();
    }
    public double getDensity() {
        if(tests.size() ==0) {
            return 1.0;
        }
        return getPassed()/getTests();
    }
    
    public List<UnitTestResultModel> getUnitTests() {
        return tests;
    }
}
