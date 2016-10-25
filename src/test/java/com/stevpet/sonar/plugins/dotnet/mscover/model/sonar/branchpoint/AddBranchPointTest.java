/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.branchpoint;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarBranchPoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarBranchPoints;

public class AddBranchPointTest {

    private SonarBranchPoints branchPoints;

    @Before
    public void before() {
        branchPoints = new SonarBranchPoints();
    }
    
    @Test
    public void assignBranchPoints_noPointAdded() {
        assertEquals(branchPoints.size(),0);
    }
    
    @Test
    public void addFirstPoint_isBranchPoint() {
        branchPoints.addPoint(10,false);
        assertLast(1,10,0,1);
    }
    
    @Test
    public void addSecondPointSameLine_CheckAggregation() {
        branchPoints.addPoint(10,false);
        branchPoints.addPoint(10, true);
        
        assertLast(1,10,1,2);
    }
    
    @Test
    public void addThirdPointOtherLine_CheckAggregation() {
        branchPoints.addPoint(10,false);
        branchPoints.addPoint(10, true);
        branchPoints.addPoint(11,true);
        assertLast(2,11,1,1);
    }
    
  
    @Test
    public void addFourthPointOtherLine_CheckAggregation() {
        branchPoints.addPoint(10,false);
        branchPoints.addPoint(10, true);
        branchPoints.addPoint(11,true);
        branchPoints.addPoint(11, true);
        assertLast(2,11,2,2);
    }
    /**
     * assert that the parameters are equal to the values of the last branchpoint
     * @param size number of branchpoints
     * @param line
     * @param branchesVisited
     * @param branchesToVisit
     */
    public void assertLast(int size,int line,int branchesVisited,int branchesToVisit) {
        //TODO remove cast
        SonarBranchPoint branchPoint=(SonarBranchPoint) branchPoints.getLast();
        assertEquals("size",size,branchPoints.size());
        assertEquals("line",line,branchPoint.getLine());
        assertEquals("branches visited",branchesVisited,branchPoint.getCovered());
        assertEquals("branches to visit",branchesToVisit,branchPoint.getToCover());       
    }
}
