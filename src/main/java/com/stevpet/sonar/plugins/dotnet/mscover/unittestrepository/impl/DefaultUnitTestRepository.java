package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTest;

public class DefaultUnitTestRepository implements UnitTestRepository {

    List<UnitTest> unitTests = new ArrayList<>() ;
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.UnitTestRepository#addTestResult(com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTest)
     */
    @Override
    public void addUnitTest(UnitTest unitTest) {
        unitTests.add(unitTest);
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl.UnitTestRepository#getUnitTests(com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId)
     */
    @Override
    public List<UnitTest> getUnitTests(MethodId methodId) {
        List<UnitTest> result = new ArrayList<>(); 
        unitTests.stream().filter( v -> v.hasTestMethod(methodId)).forEach(v -> result.add(v));
        return result ;
    }
}
