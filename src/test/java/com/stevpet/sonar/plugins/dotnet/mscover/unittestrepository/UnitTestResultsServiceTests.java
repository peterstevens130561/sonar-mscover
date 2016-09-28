package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

public interface UnitTestResultsServiceTests {

    /**
     * test with a sourcefile that is not in the repository, the list should be empty
     */
    void unknownSourceFile();

    /**
     * test with a sourcefile that has one method, and one test
     */
    void simpleOne();
    
    /**
     * test with a case where there are no sourcesfiles, no methods, no tests, the list should be empty
     */
    void emptyRepositoriesExpectEmptyList();

    /**
     * the sourcefile exists, there are two methods, and one method has two tests.
     */
    void sourceFileHasSomeTwoMethodsAndOneMethodHasTwoTestsShouldHaveThreeTests();
}
