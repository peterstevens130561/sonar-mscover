package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

public interface CoverageRunner {

    /**
     * Invokes opencover with embedded testrunner
     */
    void execute();

}