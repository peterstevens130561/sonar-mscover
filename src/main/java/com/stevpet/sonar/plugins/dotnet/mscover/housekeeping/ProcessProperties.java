package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Properties of a process
 * @author stevpet
 *
 */
public class ProcessProperties {
    private Dictionary<String,String> properties = new Hashtable<>(60);
    
    public ProcessProperties(String[] processLines) {
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
        return get("CommandLine");
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
