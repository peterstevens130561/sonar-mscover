package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTest;

public class DefaultUnitTestRepository {

    List<UnitTest> unitTests = new ArrayList<>() ;
    
    public void addTestResult(UnitTest unitTest) {
        unitTests.add(unitTest);
    }
    
    /**
     * get all unit tests that match the given MethodId.  If no methods are found then an empty list is returned
     * @param methodId
     * @return
     */
    public List<UnitTest> getUnitTests(MethodId methodId) {
        List<UnitTest> result = new ArrayList<>(); 
        unitTests.stream().filter( v -> v.hasTestMethod(methodId)).forEach(v -> result.add(v));
        return result ;
    }
}
