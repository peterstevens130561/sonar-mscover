package com.stevpet.sonar.plugins.dotnet.mscover.repositories;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.model.UnitTest;

public interface UnitTestRepository {

    void addUnitTest(UnitTest unitTest);

    /**
     * get all unit tests that match the given MethodIds.  If no methods are found then an empty list is returned
     * @param methods
     * @return
     */
    List<UnitTest> getUnitTests(MethodIds methods);

    List<UnitTest> getUnitTests(MethodId methodId);

}