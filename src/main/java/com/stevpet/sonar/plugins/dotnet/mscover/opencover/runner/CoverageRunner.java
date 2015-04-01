package com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner;

public interface CoverageRunner {

    /**
     * Invokes opencover with embedded testrunner
     */
    void execute();

    /**
     * 
     * @return the output of stdout after execute
     */
    String getStdOut();

}