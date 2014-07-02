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
        SonarBranchPoint branchPoint=branchPoints.getLast();
        assertEquals("size",size,branchPoints.size());
        assertEquals("line",line,branchPoint.getLine());
        assertEquals("branches visited",branchesVisited,branchPoint.getCovered());
        assertEquals("branches to visit",branchesToVisit,branchPoint.getToCover());       
    }
}
