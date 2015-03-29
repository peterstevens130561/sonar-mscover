package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

public class CommandLineExexutorStub implements CommandLineExecutor {

    String commandLine ;
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

}
