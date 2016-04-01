package com.stevpet.sonar.plugins.common.commandexecutor;

public class CommandExecutors {
    public CommandExecutor create() {
        return new SonarCommandExecutor();
    }
}
