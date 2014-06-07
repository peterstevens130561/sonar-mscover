package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;

public interface CoverageRegistry {

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
    void addCoveredLine(int fileId, CoveragePoint point);
    
     Collection<FileCoverage> getFileCoverages();
    

    int getFileCount();

    int getLineCount();

    int getCoveredLineCount();
    
    void addUnCoveredLine(Integer sourceFileID,
            CoveragePoint point);

}