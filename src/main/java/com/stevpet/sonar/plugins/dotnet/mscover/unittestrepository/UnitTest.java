package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTestMethodResult;

public interface UnitTest {

    boolean hasTestMethod(MethodId methodId);

    public String getTestName() ;
    
    public UnitTestMethodResult getUnitTestResult() ;
}
