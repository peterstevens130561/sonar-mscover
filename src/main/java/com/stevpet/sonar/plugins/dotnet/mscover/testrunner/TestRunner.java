package com.stevpet.sonar.plugins.dotnet.mscover.testrunner;

import java.io.File;
import java.util.regex.Pattern;

import org.sonar.api.BatchExtension;

public interface TestRunner extends BatchExtension {
    /**
     * Runs the tests, with code coverage
     */
    void execute();
    /**
     * @return file that has the test results.
     */
    File getTestResultsFile();
    /**
     * sets the file which holds the coverage data
     */
	void setCoverageFile(File coverageFile);
	/**
	 * unit test pattern
	 * @param pattern
	 */
    void setTestProjectPattern(Pattern pattern);
}
