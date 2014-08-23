package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.OpenCoverSequencePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.model.SequencePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.BranchOffsetToLineMapper;

public class BranchOffsetToLineMapperTest {
    
    private BranchOffsetToLineMapper mapper;
    @Before
    public void before() {
        mapper = new BranchOffsetToLineMapper() ;
    }
    
    @Test(expected=SonarException.class)
    public void requestMapWithoutSequencePoint_ShouldThrowException() {
        mapper.mapOffsetToLine(20);  
    }
    
    @Test(expected=SonarException.class)
    public void requestMapAftrerStart_ShouldThrowException() {
        mapper.mapOffsetToLine(20);  
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
    
    @Test(expected=SonarException.class)
    public void startAfterSeveralPointsFollowedByClear_ExpectEmptyList() {
        addPoint("0","64");
        addPoint("10","66");
        mapper.start();
        
       mapper.mapOffsetToLine(10);
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
