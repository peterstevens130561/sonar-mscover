package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class SourceFile {

    private String path ;
    public SourceFile(String path) {
        this.path = path;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o==null) {
            return false;
        }
        if(!(o instanceof SourceFile)) {
            return false;
        }
        SourceFile otherId = (SourceFile)o;
        return path.equals(otherId.path);
    }
    
    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
