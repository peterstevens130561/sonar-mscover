package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFile;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository;


public class DefaultSourceFileRepository implements SourceFileRepository {
    List<SourceFile> sourceFiles = new ArrayList<>() ;
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository#addFile(java.lang.String, java.lang.String)
     */
    @Override
    public void addFile(String fileId, String filePath) {
        
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository#getSourceFiles()
     */
    @Override
    public List<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    @Override
    public String getId(String filePath) {
        // TODO Auto-generated method stub
        return null;
    }
}
