/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.BranchOffsetToLineMapper;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.OpenCoverSequencePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.SequencePoint;

public class BranchOffsetToLineMapperTest {
    
    private BranchOffsetToLineMapper mapper;
    @Before
    public void before() {
        mapper = new BranchOffsetToLineMapper() ;
    }
    
    @Test
    public void requestMapWithoutSequencePoint_ShouldThrowException() {
        int line=mapper.mapOffsetToLine(20);
        assertEquals(-1, line);
    }
    
    @Test
    public void requestMapAftrerStart_ShouldThrowException() {
        int line=mapper.mapOffsetToLine(20); 
        assertEquals(-1, line);
    }
    
    @Test
    public void addSequencePointRequestSamePoint_ShouldBeSame() {
        addPoint("0","64");

        int lineFound = mapper.mapOffsetToLine(0);

        assertEquals(lineFound,64);
                
    }
    
   

    @Test
    public void addSequencePointsRequestLastPoint_ShouldBeLast() {
        addPoint("0","64");
        addPoint("10","66");
  
        int lineFound = mapper.mapOffsetToLine(10);
 
        assertEquals(lineFound,66);               
    }
    
    
    @Test
    public void addSequencePointsRequestBeforeLastPoint_ShouldBeFirst() {
        addPoint("0","64");
        addPoint("10","66");
  
        int lineFound = mapper.mapOffsetToLine(8);
 
        assertEquals(lineFound,64);               
    }
    
    @Test
    public void startAfterSeveralPointsFollowedByClear_ExpectEmptyList() {
        addPoint("0","64");
        addPoint("10","66");
        mapper.init();
        
       int line=mapper.mapOffsetToLine(10);
       assertEquals(-1,line);
    }
    
    @Test
    public void addSeriesRequestPoints_checkLine() {
        // offset line
        addPoint("0","10");
        addPoint("10","13");
        addPoint("13","14");
        addPoint("16","15");
        
       // offset line
       assertMap(0,10);
       assertMap(5,10);
       assertMap(9,10);
       assertMap(10,13);
       assertMap(11,13);
       assertMap(13,14);
       assertMap(16,15);

    }
 
    private void assertMap(int givenOffset,int expectedLine) {
        int lineFound = mapper.mapOffsetToLine(givenOffset);
        assertEquals(expectedLine,lineFound);
    }
    /**
     * 
     * @param offset
     * @param line
     */
    private void addPoint(String offset, String line) {
        SequencePoint sequencePoint = new OpenCoverSequencePoint() ;
        sequencePoint.setStartLine(line);
        sequencePoint.setOffset(offset);
        mapper.addSequencePoint(sequencePoint);
    }
}
