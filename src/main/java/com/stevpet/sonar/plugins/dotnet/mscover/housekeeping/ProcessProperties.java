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
    
    public void put(Property property) {
        properties.put(property.getKey(),property.getValue());
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
