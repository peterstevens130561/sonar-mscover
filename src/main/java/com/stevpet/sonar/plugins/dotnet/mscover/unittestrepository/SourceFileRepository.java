package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository;

import java.util.List;

public interface SourceFileRepository {

    void addFile(String fileId, String filePath);

    List<SourceFile> getSourceFiles();

    String getId(String filePath);

}