package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

public interface ProcessLock {

    /**
     * Creates a lock in the TMP directory
     * @param lockName
     */
    void lock(String lockName);

    void release();

}