package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class FileId {

    private final String id;
    public FileId(String id) {
        if(id==null) {
            throw new IllegalArgumentException("id");
        }
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(o==null) {
            return false;
        }
        if(!(o instanceof FileId)) {
            return false;
        }
        FileId otherId = (FileId)o;
        return id.equals(otherId.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
   
}
