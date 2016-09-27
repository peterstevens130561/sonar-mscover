package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import java.util.List;

public interface UnitTestResultService {

    /**
     * given an absolutefilePath, find all the unit tests in this file, with their results.
     * @param absoluteFilePath
     * @return
     */
    List<UnitTest> getUnitTestsFor(String absoluteFilePath);

}
