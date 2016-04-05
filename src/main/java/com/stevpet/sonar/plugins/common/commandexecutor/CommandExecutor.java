package com.stevpet.sonar.plugins.common.commandexecutor;

import org.sonar.api.utils.command.StreamConsumer;
import org.sonar.api.utils.command.Command;
public interface CommandExecutor {
    
    /**
     * Execute the prepared command in command. 
     * @param command
     * @param stdOut - receives stdout of command
     * @param stdErr - receives stderr of command
     * @param timeoutMilliseconds - timeout of the command
     * 
     * @return
     */
    public int execute(Command command, StreamConsumer stdOut, StreamConsumer stdErr, long timeoutMilliseconds);
    public int execute(Command command, long timeoutMilliseconds);
    
}
