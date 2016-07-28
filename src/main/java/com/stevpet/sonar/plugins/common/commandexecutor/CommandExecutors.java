package com.stevpet.sonar.plugins.common.commandexecutor;

public class CommandExecutors {
    public CommandExecutor create() {
        //return new ImpatientCommandExecutor();
        return new MonitoringCommandExecutor();
    }
    
    public CommandExecutor createOld() {
        return new SonarCommandExecutor();        
    }
}
