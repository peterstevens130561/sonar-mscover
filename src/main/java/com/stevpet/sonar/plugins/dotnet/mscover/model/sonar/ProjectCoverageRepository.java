package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import java.util.Collection;

public interface ProjectCoverageRepository {

    /**
     * 
     * @param id of file
     * @return either a new FileCoverage, or the one found
     */
    SonarFileCoverage getCoverageOfFile(String id);

    boolean equals(Object other);

    boolean fileIdExists(String fileID);

    Collection<SonarFileCoverage> getValues();

    /**
     * 
     * @return number of files listed in the coverage report
     */
    int size();

    /**
     * Merge the given repository into this repository
     * @param otherRepository
     * @throws SonarCoverageMergeException in case a file is encountered that is already in the repository, but has a different number of lines
     */
    void mergeIntoThis(ProjectCoverageRepository otherRepository);

    /**
     * Create an entry to map a sourcefileName (key) to the fileId (value). 
     * If the entry already exists it is overwritten
     * @param sourceFileName
     * @param fileId
     */
    void linkFileNameToFileId(String sourceFileName, String fileId);

    void addBranchPoint(String fileId, int branchLine, int branchPath, boolean branchVisited);

    void addLinePoint(String uid, int sequencePointLine, boolean lineVisited);

}