package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.ShellCommand;

public interface CommandLineExecutor {

    /**
     * Execute a windows commandline command. Sets new stdout / stderr
     * @param command
     * @return exit code (0=ok)
     */
    public abstract int execute(ShellCommand command);

    /**
     * 
     * @return stdout result. after execute.
     */
    public abstract String getStdOut();

    /**
     * 
     * @return stderr result after execute
     */
    public abstract String getStdErr();

}