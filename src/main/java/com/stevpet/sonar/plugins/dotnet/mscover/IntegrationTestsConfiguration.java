package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;


public interface IntegrationTestsConfiguration {

    public enum Mode {
    	DISABLED, // Do not run
    	RUN, // Run the tests
    	READ, // Read results, and save
    	ACTIVE, // Read or Run
    	AUTO,// sets mode for the project depending on whether the project is below the root (RUN) or not (READ)
    }
    
    public enum Tool {
    	VSTEST,OPENCOVER
    }

    public void validate();
    public static final int TESTRUNNER_THREADS_DEFAULT = 1;
    public static final int TESTRUNNER_TIMEOUT_DEFAULT = 10;
    
	/**
	 * get the mode, plugins should use this to select where they should run or not, in case of automode it will resolv it run if the current
	 * module is below the root, otherwise read
	 * @return
	 */
	Mode getMode();

    
	/**
	 * get the directory where coverage files are stored. Use when it has been verified that
	 * the mode is not disabled
	 * @return
	 * @throws IllegalArgumentException when not specified
	 */
	File getDirectory();

	/**
	 * get the tool to use when running or reading 
	 * @return
	 */
	Tool getTool();

	boolean matches(Tool tool, Mode mode);

	/**
	 * specification of testcasefilter, according to the test runner used.
	 * @return
	 */
	String getTestCaseFilter();

	/**
	 * the regularexpression pattern for integration test projects
	 * @return
	 */

    Pattern getTestProjectPattern();

    int getCoverageReaderTimeout();

    int getCoverageReaderThreads();

    /**
     * Max number of threads that the testrunner may use, default is 1
     * @return
     */
    int getTestRunnerThreads();

    /**
     * timeout of the testrunner in minutes, default is 10
     */
    int getTestRunnerTimeout();

    /**
     * the schedule pattern, 1=MONDAY, 2=TUESDAY i.e. * or empty is each day 
     * @return
     */
    Pattern getSchedule();

    /**
     * get projects to exclude from running. If not defined, then it should be an empty list
     * @return
     */
    List<String> getExcludedProjects();


    /**
     * number of retries to perform. If not defined then it should be 0
     * @return
     */
    public int getRetries();
}