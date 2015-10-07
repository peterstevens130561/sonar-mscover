package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;

public class CommandLineExexutorStub implements CommandLineExecutor {

    String commandLine ;
    private int timeoutMinutes;
    @Override
    public int execute(ShellCommand command) {
        if(command !=null) {
            commandLine=command.toCommandLine();
        }
        return 0;
    }

    @Override
    public String getStdOut() {
        return null;
    }

    @Override
    public String getStdErr() {
        return null;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public int getTimeoutMinutes() {
        return timeoutMinutes;
    }
    @Override
    public int execute(ShellCommand command, int timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
        if(command !=null) {
            commandLine=command.toCommandLine();
        }
        return 0;
    }

}
