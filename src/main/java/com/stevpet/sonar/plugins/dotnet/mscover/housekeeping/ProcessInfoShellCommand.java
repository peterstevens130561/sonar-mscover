package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.command.Command;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.common.api.ShellCommand;

public class ProcessInfoShellCommand implements ShellCommand {

    private String name;

    public void setProcessName(String name) {
        this.name=name;
    }
    @Override
    public String toCommandLine() {
        // TODO Auto-generated method stub
        return toCommand().toString();
    }

    @Override
    public Command toCommand() {
        Preconditions.checkState(StringUtils.isNotEmpty(name),"name not set");
        Command command = Command.create("WMIC");
        command.addArgument("process");
        command.addArgument("where");
        command.addArgument("name=\"" + name + "\"");
        command.addArgument("get");
        command.addArgument("processid,parentprocessid,name");
        return command;
    }

    @Override
    public String getExecutable() {
        // TODO Auto-generated method stub
        return null;
    }

}
