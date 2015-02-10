package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.LineCoverageRegistry;

public class TestCoverageRegistry implements LineCoverageRegistry {

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
    }

    public int getFileCount() {
        return visitedFiles;
    }

    public Object getVisitedFiles() {
        return visitedFiles;
    }

    public Object getVisitedLines() {
        return coveredLines;
    }

    public int getLineCount() {
        return coveredLines;
    }

    public Collection<FileCoverage> getFileCoverages() {
        return null;
    }

    public void addUnCoveredLine(Integer sourceFileID, CoveragePoint point) {
        uncoveredLines++;
        
    }

    public int getCoveredLineCount() {
        return coveredLines + uncoveredLines;
    }

}
