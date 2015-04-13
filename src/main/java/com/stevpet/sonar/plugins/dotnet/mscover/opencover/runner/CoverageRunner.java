package com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner;

public interface CoverageRunner {

    /**
     * Invokes opencover with embedded testrunner
     */
    void execute();


}