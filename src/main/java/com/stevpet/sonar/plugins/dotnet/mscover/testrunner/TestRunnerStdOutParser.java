package com.stevpet.sonar.plugins.dotnet.mscover.testrunner;

import java.io.File;

import org.sonar.api.BatchExtension;

public interface TestRunnerStdOutParser extends BatchExtension {
    /**
     * set the stdout of the test runner step, to be used to retrieve test
     * results file & coverage file
     */
    void setStdOut(String stdOut);

    /**
     * @return file that has the test results
     */
    File getTestResultsFile();

    /**
     * @return file that has the coverage info, as provided by the test runner
     */
    File getCoverageFile();

}
