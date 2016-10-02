package com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.stevpet.sonar.plugins.dotnet.mscover.model.impl.DefaultSourceFile;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;


public class DefaultSourceFileRepository implements SourceFileRepository {
    List<DefaultSourceFile> sourceFiles = new ArrayList<>() ;
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository#addFile(java.lang.String, java.lang.String)
     */
    @Override
    public void add(String fileId, String filePath) {
        sourceFiles.add(new DefaultSourceFile(fileId,filePath));
    }
    
    @Override
    public String getId(String filePath) {
        Optional<DefaultSourceFile> match= sourceFiles.stream().filter(f -> f.getPath().equals(filePath)).findFirst();
        return match.isPresent()?match.get().getId():null;
    }
}
