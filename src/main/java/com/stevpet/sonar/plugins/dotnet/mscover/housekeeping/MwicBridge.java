/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
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

        AllProcessInfoMwicCommand processInfoCommand = new AllProcessInfoMwicCommand();
        processInfoCommand.setProcessName(processName);
        commandLineExecutor.execute(processInfoCommand);
        String result = commandLineExecutor.getStdOut();
        ProcessesProperties processesInfo = new ProcessesProperties(result);
        return processesInfo;
    }

    /**
     * kill process by id 
     * @param anyString - if null, then nothing is done
     */
    public void killProcessId(String processId) {
        if(processId==null) {
            return;
        }
        ProcessKillShellCommand processKillCommand = new ProcessKillShellCommand(processId);
        commandLineExecutor.execute(processKillCommand);
    }

}
