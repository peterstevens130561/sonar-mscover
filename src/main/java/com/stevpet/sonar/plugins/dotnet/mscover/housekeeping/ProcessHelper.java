package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;

public class ProcessHelper {

    private CommandLineExecutor commandLineExecutor;

    public ProcessHelper(CommandLineExecutor commandLineExecutor) {
        this.commandLineExecutor = commandLineExecutor;
    }

    List<ProcessInfo> getProcessInfoFromName(String processName) {

        ProcessInfoShellCommand processInfoCommand = new ProcessInfoShellCommand();
        processInfoCommand.setProcessName(processName);
        commandLineExecutor.execute(processInfoCommand);
        String result = commandLineExecutor.getStdOut();

        List<ProcessInfo> processesInfo = new ArrayList<ProcessInfo>();
        if (StringUtils.isEmpty(result)) {
            return processesInfo;
        }

        String[] lines = result.split("\r\n");
        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split("\\s+");
            if (fields.length != 3) {
                continue;
            }
            ProcessInfo processInfo = new ProcessInfo(fields[0], fields[2], fields[1]);
            processesInfo.add(processInfo);
        }
        return processesInfo;

    }

    ProcessesProperties getProcessPropertiesForName(String processName) {

        AllProcessInfoShellCommand processInfoCommand = new AllProcessInfoShellCommand();
        processInfoCommand.setProcessName(processName);
        commandLineExecutor.execute(processInfoCommand);
        return collectProperties();
    }

    private ProcessesProperties collectProperties() {
        String result = commandLineExecutor.getStdOut();

        ProcessesProperties processesInfo = new ProcessesProperties();
        if (StringUtils.isEmpty(result)) {
            return processesInfo;
        }

        String[] lines = result.split("\r\n");
        ProcessProperties processProperties = null;
        for (int i = 1; i < lines.length; i++) {
            while(i<lines.length && lines[i].isEmpty()) {
                i++;
            }
            if(i==lines.length) {
                continue;
            }
            processProperties = new ProcessProperties();    
            while(i<lines.length && !lines[i].isEmpty()) {
                Property property = new Property(lines[i]);
                processProperties.put(property);        
                i++;
            }
            processesInfo.put(processProperties);
        }
        return processesInfo;
    }

    public void killProcess(String anyString) {
        ProcessKillShellCommand processKillCommand = new ProcessKillShellCommand();
        processKillCommand.setProcessId(anyString);
        commandLineExecutor.execute(processKillCommand);
    }

}
