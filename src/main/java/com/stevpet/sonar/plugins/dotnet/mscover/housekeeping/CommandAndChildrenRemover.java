package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.List;

public class CommandAndChildrenRemover {
    private ProcessHelper processHelper ;
    
    public void cancel(String commandLine) {
        ProcessesProperties properties = processHelper.getProcessPropertiesForName("opencover.console.exe");
        if(properties.size()==0) {
            return;
        }
        String processId = properties.getProcessIdOfCommandLine(commandLine);
        ProcessesProperties vsTestProperties=processHelper.getProcessPropertiesForName("vstest.console.exe");
        String vstTestId=properties.getProcessIdOfChildOf(processId);
        
        
    }
}
