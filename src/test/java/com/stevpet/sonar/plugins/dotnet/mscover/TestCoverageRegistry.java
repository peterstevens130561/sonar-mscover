package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;
import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;

public class TestCoverageRegistry implements CoverageRegistry {

    private int visitedFiles;
    private int coveredLines;
    private int uncoveredLines;

    public void addFile(int fileId, String filePath) {
        visitedFiles++;
    }

    public void addCoveredLine(int fileId, CoveragePoint point) {
        coveredLines++;       
    }

    public void save() {
        // TODO Auto-generated method stub
        
    }

    public int getFileCount() {
        // TODO Auto-generated method stub
        return visitedFiles;
    }

    public Object getVisitedFiles() {
        // TODO Auto-generated method stub
        return visitedFiles;
    }

    public Object getVisitedLines() {
        // TODO Auto-generated method stub
        return coveredLines;
    }

    public int getLineCount() {
        // TODO Auto-generated method stub
        return coveredLines;
    }

    public Collection<FileCoverage> getFileCoverages() {
        // TODO Auto-generated method stub
        return null;
    }

    public void addUnCoveredLine(Integer sourceFileID, CoveragePoint point) {
        uncoveredLines++;
        
    }

    public int getCoveredLineCount() {
        // TODO Auto-generated method stub
        return coveredLines + uncoveredLines;
    }

}
