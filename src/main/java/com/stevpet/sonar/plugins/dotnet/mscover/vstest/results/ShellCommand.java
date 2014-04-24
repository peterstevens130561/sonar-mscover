package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import org.sonar.api.utils.command.Command;

public interface ShellCommand {
    String toCommandLine();
    Command toCommand();
}
