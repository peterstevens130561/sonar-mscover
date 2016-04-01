package com.stevpet.sonar.plugins.common.commandexecutor;

import org.sonar.api.utils.command.Command;
import org.sonar.api.utils.command.StreamConsumer;

public interface CommandExecutor {
    public int execute(Command command, StreamConsumer stdOut, StreamConsumer stdErr, long timeoutMilliseconds);
    public int execute(Command command, long timeoutMilliseconds);
    
}
