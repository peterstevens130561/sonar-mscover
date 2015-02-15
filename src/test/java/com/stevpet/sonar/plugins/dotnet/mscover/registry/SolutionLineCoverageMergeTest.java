package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import org.junit.Assert;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.CoveragePoint;

public class SolutionLineCoverageMergeTest {

    @Test
    public void SimpleTest() {
        SolutionLineCoverage dest, source;
        int[] coverageDest = { 1,0,1 ,0};
        int[] coverageSource = {0,0,1,1};
        int[] coverageExpected= {1,0,2,1};
        dest=createCoverage(1,coverageDest);
        source=createCoverage(5,coverageSource);
        dest.merge(1,5,source);
        destCoverageMatches(1,coverageExpected,dest);
    }
    
    private SolutionLineCoverage createCoverage(int fileId,int [] coverage) {
        SolutionLineCoverage result = new DefaultSolutionLineCoverage("hi");
            CoveragePoint point = new CoveragePoint();   
            for(int i=0;i<coverage.length;i++) {
                point.setStartLine(i);
                point.setEndLine(i);
                if(coverage[i]>0) {
                    result.addCoveredFileLine(fileId, point);
                } else {
                    result.addUnCoveredFileLine(fileId,point);
                }
            }
            return result;
        }

private void destCoverageMatches(int sourceId,int[] coverageResult,SolutionLineCoverage actual) {
    for(int i=0;i<coverageResult.length;i++) {
        int actualVisited=actual.getFileLineCoverage(sourceId).getLines().get(i).getVisits();
        int expectedVisited=coverageResult[i];
        Assert.assertEquals("at index " + i + " expect " + coverageResult[i] + " got " + actualVisited,expectedVisited,actualVisited);
    }
}
}
