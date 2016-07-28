package com.stevpet.sonar.plugins.dotnet.mscover.controltest;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

public interface TestExecutorMonitor extends Callable<ENDSTATE> {

    /**
     * 
     * @param testRunner the testRunner to monitor
     */
    public void setTestRunner(@Nonnull TestRunner testRunner);


}
