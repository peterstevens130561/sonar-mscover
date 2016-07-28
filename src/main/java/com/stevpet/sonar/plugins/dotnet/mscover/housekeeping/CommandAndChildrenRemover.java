package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

public class CommandAndChildrenRemover {
    private static Logger LOG = LoggerFactory.getLogger(CommandAndChildrenRemover.class);
    private CommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor();
    private MwicBridge processHelper = new MwicBridge(commandLineExecutor);

    public void cancel(String commandLine) {
        try {
        ProcessesProperties properties = processHelper.getProcessPropertiesForName("opencover.console.exe");
        String openCoverProcessId = properties.getProcessIdOfCommandLine(commandLine);
        
        ProcessesProperties vsTestProperties = processHelper.getProcessPropertiesForName("vstest.console.exe");
        String vsTestId = vsTestProperties.getProcessIdOfChildOf(openCoverProcessId);
        
        ProcessesProperties teProperties = processHelper.getProcessPropertiesForName("TE.ProcessHost.Managed.exe");
        String teId = teProperties.getProcessIdOfChildOf(vsTestId);
        processHelper.killProcessId(teId);
        processHelper.killProcessId(vsTestId);
        processHelper.killProcessId(openCoverProcessId);
        } catch ( Exception e ) {
            LOG.error("Exception thrown during cancelling process {}",e.getMessage());
        }
    }
}
