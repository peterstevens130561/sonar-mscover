package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import java.util.Collection;

public interface ProjectCoverageRepository {

    SonarFileCoverage getCoverageOfFile(String id);

    boolean fileIdExists(String fileID);

    Collection<SonarFileCoverage> getValues();

    int size();

    void mergeIntoThis(ProjectCoverageRepository otherRepository);

    void linkFileNameToFileId(String sourceFileName, String fileId);

    void addBranchPoint(String fileId, int line, int path, boolean visited);

    void addLinePoint(String fileId, int line, boolean visited);

}
