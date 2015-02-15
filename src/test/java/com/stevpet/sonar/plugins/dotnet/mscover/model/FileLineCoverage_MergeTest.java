package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.junit.Assert;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class FileLineCoverage_MergeTest {
    
    @Test
    public void MergeAllCombinations_ShouldMatch() {
        FileLineCoverage dest,source;
        //Given
        int[] coverageDest = { 1,0,1,0 };
        int[] coverageSource = {0,0,1,1};
        dest=createCoverage(coverageDest);
        source=createCoverage(coverageSource);
        //When
        dest.merge(source);
        int[] coverageResult = {1,0,2,1};
        //Then
        destCoverageMatches(coverageResult,dest);
        
    }
    
    @Test(expected=MsCoverException.class)
    public void MergeDifferentNumberOfLines_ExpectException() {
        FileLineCoverage dest,source;
        //Given
        int[] coverageDest = { 1,0,1 };
        int[] coverageSource = {0,0,1,1};
        dest=createCoverage(coverageDest);
        source=createCoverage(coverageSource);
        //When
        dest.merge(source);
        
    }

    private void destCoverageMatches(int[] coverageResult,FileLineCoverage actual) {
        for(int i=0;i<coverageResult.length;i++) {
            int actualVisited=actual.getLines().get(i).getVisits();
            int expectedVisited=coverageResult[i];
            Assert.assertEquals("at index " + i + " expect " + coverageResult[i] + " got " + actualVisited,expectedVisited,actualVisited);
        }
    }

    private FileLineCoverage createCoverage(int [] coverage) {
        FileLineCoverage result = new FileLineCoverage(1);
        CoveragePoint point = new CoveragePoint();   
        for(int i=0;i<coverage.length;i++) {
            point.setStartLine(i);
            point.setEndLine(i);
            if(coverage[i]>0) {
                result.addCoveredLine(point);
            } else {
                result.addUnCoveredLine(point);
            }
        }
        return result;
    }
    
}
