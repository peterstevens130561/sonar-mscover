package com.stevpet.sonar.plugins.dotnet.mscover.testrunner;

import java.io.File;

public interface TestRunner {
    /**
     * Runs the tests, with code coverage
     */
    void execute();

    /**
     * @return file that has the test results
     */
    File getTestResultsFile();
}
