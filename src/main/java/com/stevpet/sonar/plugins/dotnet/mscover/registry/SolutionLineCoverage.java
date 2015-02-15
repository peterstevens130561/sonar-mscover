package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;

public interface SolutionLineCoverage {

    /**
     * add a file to the coverage
     * @param fileId unique id of the file
     * @param sourceFilePath full path to the file
     */
    void addFile(int fileId, String sourceFilePath);

    /**
     *  Add point to the coverage registry for the given file
     * @param fileId unique id of the file
     * @param point coveragepoint of the line
     */
    void addCoveredFileLine(int fileId, CoveragePoint point);
    
     Collection<FileLineCoverage> getFileCoverages();
    

    int getFileCount();

    int getLineCount();

    int getCoveredLineCount();
    
    void addUnCoveredFileLine(Integer sourceFileID,
            CoveragePoint point);

    /**
     * merge the coverage info of the file with toMergeId Id of the sourceSoluctionLineCoverage, into destinationID
     * @param destinationId
     * @param toMergeId
     * @param lineCoverageRegistry
     */
    void merge(int destinationId, int toMergeId, SolutionLineCoverage lineCoverageRegistry);

    FileLineCoverage getFileLineCoverage(int sourceId);

}