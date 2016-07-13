package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        String[] processBlock = result.split("\r\n\r\n");
        ProcessProperties processProperties = null;
        for (int block = 1; block < processBlock.length; block++) {
            processProperties = new ProcessProperties(processBlock[block]);
            processesProperties.add(processProperties);
        }
    }
   
  
    public String getProcessIdOfCommandLine(String commandLine) {
        Optional<ProcessProperties> process = processesProperties.stream().filter(p -> p.getCommandLine().equals(commandLine)).findFirst();
        return process.isPresent() ? process.get().getProcessId() : null;
    }
    
    
    public String getProcessIdOfChildOf(String parentId) {
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
