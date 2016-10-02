package com.stevpet.sonar.plugins.dotnet.mscover.model;

public interface UnitTest {

    boolean hasTestMethod(MethodId methodId);

    public String getTestName() ;
    
    public UnitTestMethodResult getUnitTestResult() ;
}
