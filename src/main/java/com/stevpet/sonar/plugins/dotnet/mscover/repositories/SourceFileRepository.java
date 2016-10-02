package com.stevpet.sonar.plugins.dotnet.mscover.repositories;

import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.impl.DefaultSourceFile;

public interface SourceFileRepository {

    void addFile(String fileId, String filePath);

    List<DefaultSourceFile> getSourceFiles();

    String getId(String filePath);

}