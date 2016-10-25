/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.apache.commons.lang.StringUtils;


public class ProcessesProperties {
    List<ProcessProperties> processesProperties = new ArrayList<>();
    
    /**
     * given the output of MWIC, parse it process by process, and store it for later querying
     * 
     * @param result - the MWIC output
     */
    public ProcessesProperties(String result) {
        if (StringUtils.isEmpty(result)) {
            return;
        }
        String[] processBlock = result.split("\r\n\r\n\r\n\r\n");
        ProcessProperties processProperties = null;
        for (int block = 1; block < processBlock.length; block++) {
            processProperties = new ProcessProperties(processBlock[block]);
            processesProperties.add(processProperties);
        }
    }
   
  
    public String getProcessIdOfCommandLine(String commandLine) {
        String normalizedCommandLine=normalizeCommandLine(commandLine);
        Optional<ProcessProperties> process = processesProperties.stream().filter(p -> p.getCommandLine().equals(normalizedCommandLine)).findFirst();
        return process.isPresent() ? process.get().getProcessId() : null;
    }
    
    
    private String normalizeCommandLine(String commandLine) {
        int commandEnd=commandLine.indexOf(".Exe ");
        String result=commandLine.substring(0, commandEnd+5).replace("/", "\\") + commandLine.substring(commandEnd+5);
        return result;
        
    }
    


    public String getProcessIdOfChildOf(String parentId) {
        if(parentId==null) {
            return null;
        }
        Optional<ProcessProperties> process = processesProperties.stream().filter(p -> p.getParentProcessId().equals(parentId)).findFirst();
        return process.isPresent() ? process.get().getProcessId() : null;        
    }

    public int size() {
        return processesProperties.size();
    }

    /**
     * convenience method for testing
     * @param i
     * @return
     */
    ProcessProperties get(int i) {
        return processesProperties.get(i);
    }
}
