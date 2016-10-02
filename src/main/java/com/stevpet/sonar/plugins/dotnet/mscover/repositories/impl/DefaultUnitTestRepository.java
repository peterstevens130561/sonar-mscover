package com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTest;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.UnitTestRepository;

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

    @Override
    public List<UnitTest> getUnitTests(MethodIds methods) {
        List<UnitTest> result = new ArrayList<>();
        methods.stream().forEach(method -> { result.addAll(getUnitTests(method));});
        return result;
    }
}
