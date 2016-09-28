package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;

public class DefaultUnitTest implements UnitTest {


    private final String testName;
    private final UnitTestMethodResult unitTestResult;

    public DefaultUnitTest(String testName,UnitTestMethodResult unitTestResult) {

        this.testName=testName;
        this.unitTestResult=unitTestResult;
    }
    @Override
    public boolean hasTestMethod(MethodId methodId) {
        return methodId.equals(methodId);
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
