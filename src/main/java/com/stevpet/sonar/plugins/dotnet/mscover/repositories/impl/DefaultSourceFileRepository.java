package com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFile;
import com.stevpet.sonar.plugins.dotnet.mscover.model.impl.DefaultSourceFile;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;


public class DefaultSourceFileRepository implements SourceFileRepository {
    List<SourceFile> sourceFiles = new ArrayList<>() ;
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository#addFile(java.lang.String, java.lang.String)
     */
    @Override
    public void add(String fileId, String filePath) {
        sourceFiles.add(new DefaultSourceFile(fileId,filePath));
    }
    
    @Override
    public String getId(String filePath) {
        Optional<SourceFile> match= sourceFiles.stream().filter(f -> f.getPath().equals(filePath)).findFirst();
        return match.isPresent()?match.get().getId():null;
    }

    @Override
    public String getSourceFileName(String fileId) {
        Optional<SourceFile> match= sourceFiles.stream().filter(f -> f.getId().equals(fileId)).findFirst();
        return match.isPresent()?match.get().getPath():null;
    }

    public Stream<SourceFile> stream() {
        return sourceFiles.stream();
    }
}
