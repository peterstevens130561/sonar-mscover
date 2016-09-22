package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

public class SourceFile {
    private String id;
    private String path;
    
    public SourceFile(String id, String path) {
        this.id=id;
        this.path=path;
        
    }
    public String getId() {
        return id;
    }
    
    public String getPath() {
        return path;
    }
    
}
