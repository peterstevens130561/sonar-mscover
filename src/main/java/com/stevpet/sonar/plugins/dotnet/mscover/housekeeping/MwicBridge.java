package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;

public class MwicBridge {

    private CommandLineExecutor commandLineExecutor;

    public MwicBridge(CommandLineExecutor commandLineExecutor) {
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

        ProcessesProperties processesInfo = new ProcessesProperties(result);

        return processesInfo;
    }

    /**
     * kill process by id
     * @param anyString
     */
    public void killProcessId(String processId) {
        ProcessKillShellCommand processKillCommand = new ProcessKillShellCommand(processId);
        commandLineExecutor.execute(processKillCommand);
    }

}
