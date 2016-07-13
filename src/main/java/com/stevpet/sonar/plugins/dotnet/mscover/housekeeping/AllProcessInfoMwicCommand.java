package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.command.Command;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.common.api.ShellCommand;

/**
 * Gives all process info of processes that match the name
 * 
 *
 */
public class AllProcessInfoMwicCommand implements ShellCommand  {
    private String name;

    /**
     * @param name full name of the process i.e. opencover.console.exe
     */
    public void setProcessName(String name) {
        this.name=name;
    }
    
    @Override
    public String toCommandLine() {
        return toCommand().toString();
    }

    @Override
    public Command toCommand() {
        Preconditions.checkState(StringUtils.isNotEmpty(name),"name not set");
        Command command = Command.create("WMIC");
        command.addArgument("process");
        command.addArgument("where");
        command.addArgument("name=\"" + name + "\"");
        command.addArgument("list");
        command.addArgument("full");
        return command;
    }

    @Override
    public String getExecutable() {
        return "WMIC";
    }
}
