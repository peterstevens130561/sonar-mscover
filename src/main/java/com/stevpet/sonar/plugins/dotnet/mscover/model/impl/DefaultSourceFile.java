package com.stevpet.sonar.plugins.dotnet.mscover.model.impl;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFile;

public class DefaultSourceFile implements SourceFile {
    private final String id;
    private final String path;
    
    public DefaultSourceFile(String id, String path) {
        this.id=id;
        this.path=path;  
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getPath() {
        return path;
    }
    
}
