package com.stevpet.sonar.plugins.common.commandexecutor;

import org.sonar.api.utils.command.StreamConsumer;
import org.sonar.api.utils.command.Command;
public class SonarCommandExecutor implements CommandExecutor {
    private final org.sonar.api.utils.command.CommandExecutor wrappedExecutor ;
  
    public SonarCommandExecutor() {
            wrappedExecutor = org.sonar.api.utils.command.CommandExecutor.create();
    }

    @Override
    public int execute(Command command, StreamConsumer stdOut, StreamConsumer stdErr, long timeoutMilliseconds) {
        return wrappedExecutor.execute(command,stdOut,stdErr,timeoutMilliseconds);
    }

    @Override
    public int execute(Command command, long timeoutMilliseconds) {
        return wrappedExecutor.execute(command, timeoutMilliseconds);
    }

}
