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
        this.id = id;
        this.parentId = parentId;
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

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof String) {
            return id.equals((String) other);
        }
        if (other instanceof ProcessInfo) {
            return id.equals(((ProcessInfo) other).getId());
        }
        return false;

    }
}
