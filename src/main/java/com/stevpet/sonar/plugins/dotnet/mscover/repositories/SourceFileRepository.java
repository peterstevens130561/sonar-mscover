package com.stevpet.sonar.plugins.dotnet.mscover.repositories;

public interface SourceFileRepository {

    /**
     * add the sourcefile to the repository. fileId should be unique, if not it will overwrite
     * @param fileId - integer
     * @param filePath - full path to a file
     */
    void add(String fileId, String filePath);

    /**
     * get the id of a file, denoted by its full path
     * @param filePath - full path to a file
     * @return - id of file, if not found then null
     */
    String getId(String filePath);

}