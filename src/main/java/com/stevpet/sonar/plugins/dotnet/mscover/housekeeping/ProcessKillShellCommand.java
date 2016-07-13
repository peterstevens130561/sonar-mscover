package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.command.Command;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.common.api.ShellCommand;

public class ProcessKillShellCommand implements ShellCommand {

    private String id;
    public ProcessKillShellCommand(String processId) {
        this.id=id;
    }

    @Override
    public String toCommandLine() {
        // TODO Auto-generated method stub
        return toCommand().toString();
    }

    @Override
    public Command toCommand() {
        Preconditions.checkState(StringUtils.isNotEmpty(id),"id not set");
        Command command = Command.create("WMIC");
        command.addArgument("process");
        command.addArgument("where");
        command.addArgument("processid=\"" + id + "\"");
        command.addArgument("call");
        command.addArgument("terminate");
        return command;
    }

    @Override
    public String getExecutable() {
        // TODO Auto-generated method stub
        return null;
    }

}
