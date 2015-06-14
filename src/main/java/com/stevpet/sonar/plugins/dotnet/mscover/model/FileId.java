package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class FileId implements Cloneable,Comparable<FileId> {

    private Integer id;
    public FileId(String id) {
        if(id==null) {
            throw new IllegalArgumentException("id");
        }
        this.id = Integer.parseInt(id);
    }
    
    public FileId() {
        this.id = 0;
    }

    public FileId(int i) {
        this.id = i;
    }

    public Integer getId() {
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
        return id == otherId.id;
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public Object clone() {
        return new FileId(id);
    }

    public void setNext() {
        ++id;
    }

    public int compareTo(FileId key) {
        // TODO Auto-generated method stub
        return 0;
    }
   
}
