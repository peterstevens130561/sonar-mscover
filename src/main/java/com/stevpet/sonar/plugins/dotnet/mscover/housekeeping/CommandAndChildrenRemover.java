package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;


import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

public class CommandAndChildrenRemover {
    private CommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor();
    private MwicBridge processHelper = new MwicBridge(commandLineExecutor ) ;
    
    public void cancel(String commandLine) {
        ProcessesProperties properties = processHelper.getProcessPropertiesForName("opencover.console.exe");
        if(properties.size()==0) {
            return;
        }
        String openCoverProcessId = properties.getProcessIdOfCommandLine(commandLine);
        ProcessesProperties vsTestProperties=processHelper.getProcessPropertiesForName("vstest.console.exe");
        String vsTestId=vsTestProperties.getProcessIdOfChildOf(openCoverProcessId);
        ProcessesProperties teProperties=processHelper.getProcessPropertiesForName("TE.ProcessHost.Managed.exe");
        String teID=properties.getProcessIdOfChildOf(vsTestId);      
        processHelper.killProcessId(vsTestId);
        processHelper.killProcessId(openCoverProcessId);
    }
}
