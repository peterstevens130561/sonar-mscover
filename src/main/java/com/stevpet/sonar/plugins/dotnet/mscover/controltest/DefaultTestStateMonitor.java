package com.stevpet.sonar.plugins.dotnet.mscover.controltest;

import javax.annotation.Nonnull;

import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

public class DefaultTestStateMonitor implements TestExecutorMonitor {

    private TestRunner testRunner;

    @Override
    public void setTestRunner(@Nonnull TestRunner testRunner) {
        this.testRunner = testRunner;
    }


    @Override
    public ENDSTATE call() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
