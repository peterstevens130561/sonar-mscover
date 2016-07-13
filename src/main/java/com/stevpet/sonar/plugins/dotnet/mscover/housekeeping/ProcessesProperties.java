package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class ProcessesProperties {
    List<ProcessProperties> processesProperties = new ArrayList<>();
    
    public void put(ProcessProperties processProperties) {
        this.processesProperties.add(processProperties);
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
