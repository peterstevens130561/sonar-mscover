package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.api.ShellCommand;

public class ProcessHelper {
    
    private CommandLineExecutor commandLineExecutor;

    public ProcessHelper(CommandLineExecutor commandLineExecutor) {
        this.commandLineExecutor = commandLineExecutor;
    }
    
    List<ProcessInfo> getProcessInfoFromName(String processName) {
        List<ProcessInfo> processesInfo=new ArrayList<ProcessInfo>();
        ProcessInfoShellCommand processInfoCommand = new ProcessInfoShellCommand();
        processInfoCommand.setProcessName(processName);
        commandLineExecutor.execute(processInfoCommand);
        String result=commandLineExecutor.getStdOut();
        if(StringUtils.isEmpty(result)) {
            return processesInfo;
        }
        
        String[] lines=result.split("\r\n");
        for(int i=1;i<lines.length;i++){      
            String[] fields = lines[i].split("\\s+");
            if(fields.length!=3) {
                continue;
            }
            ProcessInfo processInfo = new ProcessInfo(fields[0],fields[2],fields[1]);
            processesInfo.add(processInfo);
        }
        return processesInfo;
        
    }

    public void killProcess(String anyString) {
        ProcessKillShellCommand processKillCommand = new ProcessKillShellCommand();
        processKillCommand.setProcessId(anyString);
        commandLineExecutor.execute(processKillCommand);
    }
}
