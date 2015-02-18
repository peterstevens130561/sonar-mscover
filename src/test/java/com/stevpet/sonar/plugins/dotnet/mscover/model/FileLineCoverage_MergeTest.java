/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
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
