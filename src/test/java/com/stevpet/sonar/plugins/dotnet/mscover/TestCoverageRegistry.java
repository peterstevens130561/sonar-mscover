package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SolutionLineCoverage;

public class TestCoverageRegistry implements SolutionLineCoverage {

    private int visitedFiles;
    private int coveredLines;
    private int uncoveredLines;

    public void addFile(int fileId, String filePath) {
        visitedFiles++;
    }

    public void addCoveredFileLine(int fileId, CoveragePoint point) {
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

    public Collection<FileLineCoverage> getFileCoverages() {
        return null;
    }

    public void addUnCoveredFileLine(Integer sourceFileID, CoveragePoint point) {
        uncoveredLines++;
        
    }

    @Override
    public int getCoveredLineCount() {
        return coveredLines + uncoveredLines;
    }

    @Override
    public void merge(int destinationId, int toMergeId,
            SolutionLineCoverage lineCoverageRegistry) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public FileLineCoverage getFileLineCoverage(int sourceId) {
        // TODO Auto-generated method stub
        return null;
    }






}
