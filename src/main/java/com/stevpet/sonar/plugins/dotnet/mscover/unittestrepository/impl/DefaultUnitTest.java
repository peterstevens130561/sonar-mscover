package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTest;

public class DefaultUnitTest implements UnitTest {


    private final String testName;
    private final UnitTestMethodResult unitTestResult;

    public DefaultUnitTest(String testName,UnitTestMethodResult unitTestResult) {

        this.testName=testName;
        this.unitTestResult=unitTestResult;
    }
    @Override
    public boolean hasTestMethod(MethodId methodId) {
        return unitTestResult.getMethodId().equals(methodId);
    }

    @Override
    public String getTestName() {
        return testName;
    }

    @Override
    public UnitTestMethodResult getUnitTestResult() {

        return unitTestResult;
    }

}
