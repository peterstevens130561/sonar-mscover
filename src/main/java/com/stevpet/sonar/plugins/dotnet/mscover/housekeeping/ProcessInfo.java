package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

public class ProcessInfo {
    String name;
    String id;
    String parentId;
    
    /**
     * 
     * @param name
     * @param id
     * @param parentId
     */
    public ProcessInfo(String name, String id, String parentId) {
        this.name = name;
        this.id=id;
        this.parentId=parentId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public String getParentId() {
        return parentId;
    }
}
