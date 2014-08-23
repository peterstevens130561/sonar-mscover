package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import org.sonar.api.utils.command.Command;

public interface ShellCommand {
    String toCommandLine();
    Command toCommand();
}
