package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.command.Command;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.common.api.ShellCommand;

public class ProcessKillShellCommand implements ShellCommand {

    private final static Logger LOG = LoggerFactory.getLogger(ProcessKillShellCommand.class);
    private String id;
    public ProcessKillShellCommand(String processId) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(processId),"id not set");
        this.id=processId;
    }

    @Override
    public String toCommandLine() {
        return toCommand().toString();
    }

    @Override
    public Command toCommand() {
        LOG.info("Terminating process {}",id);
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
        return "WMIC";
    }

}
