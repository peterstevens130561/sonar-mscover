package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.UnitTest;

public interface UnitTestRepository {

    void addUnitTest(UnitTest unitTest);

    /**
     * get all unit tests that match the given MethodIds.  If no methods are found then an empty list is returned
     * @param methods
     * @return
     */
    List<UnitTest> getUnitTests(List<MethodId> methods);

    List<UnitTest> getUnitTests(MethodId methodId);

}