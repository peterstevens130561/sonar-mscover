package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import org.junit.Assert;
import org.junit.Test;


public class SolutionLineCoverageMergeTest {

    private LineCoverageTestUtilities testUtilities = new LineCoverageTestUtilities();
    @Test
    public void SimpleTest() {
        SolutionLineCoverage dest, source;
        int[] coverageDest = { 1,0,1 ,0};
        int[] coverageSource = {0,0,1,1};
        int[] coverageExpected= {1,0,2,1};
        dest=testUtilities.createCoverage(1,coverageDest);
        source=testUtilities.createCoverage(5,coverageSource);
        dest.merge(1,5,source);
        destCoverageMatches(1,coverageExpected,dest);
    }
    


private void destCoverageMatches(int sourceId,int[] coverageResult,SolutionLineCoverage actual) {
    for(int i=0;i<coverageResult.length;i++) {
        int actualVisited=actual.getFileLineCoverage(sourceId).getLines().get(i).getVisits();
        int expectedVisited=coverageResult[i];
        Assert.assertEquals("at index " + i + " expect " + coverageResult[i] + " got " + actualVisited,expectedVisited,actualVisited);
    }
}
}
