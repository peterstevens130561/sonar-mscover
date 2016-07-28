package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.Dictionary;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.internal.google.common.base.Preconditions;

/**
 * Properties of a process
 * @author stevpet
 *
 */
public class ProcessProperties {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessProperties.class);
    private Dictionary<String,String> properties = new Hashtable<>(60);
    
    public ProcessProperties(String processBlock) {
        Preconditions.checkArgument(processBlock !=null,"ProcessBlock can't be null");
        String processLines[] = processBlock.split("\r\n");
            for(int line=0;line<processLines.length;line++) {
                String currentLine = processLines[line] ;
                if(currentLine.isEmpty()) {
                    continue;
                }
                Property property = new Property(currentLine);
                properties.put(property.getKey(),property.getValue());       
            }
        }

 
    private String get(String key) {
        return properties.get(key);
    }
    
    public String getCommandLine() {
        String commandLine=get("CommandLine");
        LOG.info("CommandLine {}",commandLine);
        return commandLine;
    }

    public int size() {
        return properties.size();
    }

    public String getParentProcessId() {
        return get("ParentProcessId");
    }
    
    public String getProcessId() {
        return get ("ProcessId");
    }
}
