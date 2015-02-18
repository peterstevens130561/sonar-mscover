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
