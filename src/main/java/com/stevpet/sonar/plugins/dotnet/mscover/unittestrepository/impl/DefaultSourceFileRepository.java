package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFile;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository;


public class DefaultSourceFileRepository implements SourceFileRepository {
    List<SourceFile> sourceFiles = new ArrayList<>() ;
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository#addFile(java.lang.String, java.lang.String)
     */
    @Override
    public void addFile(String fileId, String filePath) {
        sourceFiles.add(new SourceFile(fileId,filePath));
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
        Optional<SourceFile> match= sourceFiles.stream().filter(f -> f.getPath().equals(filePath)).findFirst();
        return match.isPresent()?match.get().getId():null;
    }
}