package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.ProcessLock;

/**
 * Simple cross process locking mechanism, prevents that other sonar-runner processes execute
 * the same commands at the same time. 
 * @author stevpet
 *
 */
public class LockedWindowsCommandLineExecutor extends
        WindowsCommandLineExecutor {
    private final ProcessLock processLock;

    public LockedWindowsCommandLineExecutor(ProcessLock processLock) {
        this.processLock=processLock;
    }

    @Override
    public int execute(ShellCommand command) {
        processLock.lock();
        try {
            return super.execute(command);
        } finally {
            processLock.release();
        }
    }
}