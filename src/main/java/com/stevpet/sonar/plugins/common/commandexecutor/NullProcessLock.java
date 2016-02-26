package com.stevpet.sonar.plugins.common.commandexecutor;


public class NullProcessLock implements ProcessLock {

    @Override
    public void lock(String lockName) {

    }

    @Override
    public void release() {

    }

}
