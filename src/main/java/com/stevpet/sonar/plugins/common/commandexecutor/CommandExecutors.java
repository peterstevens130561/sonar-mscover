package com.stevpet.sonar.plugins.common.commandexecutor;

public class CommandExecutors {
    public CommandExecutor create() {
        return new ImpatientCommandExecutor();
    }
    
    public CommandExecutor createOld() {
        return new SonarCommandExecutor();        
    }
}
