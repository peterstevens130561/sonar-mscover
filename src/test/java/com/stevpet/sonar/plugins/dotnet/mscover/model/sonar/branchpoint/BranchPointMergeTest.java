package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.branchpoint;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarBranchPoint;

public class BranchPointMergeTest {

    private int LINE=5;
    @Test
    public void addSame() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(true);
    }
    
    @Test
    public void addOtherLine() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        SonarBranchPoint source = new SonarBranchPoint(LINE + 1);
        try { 
            dest.merge(source);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("different line, should have gotten IllegalArgumentException");
    }
    
    @Test
    public void mergeBothNotVisited() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(false);
        SonarBranchPoint source = new SonarBranchPoint(LINE);
        source.addPath(false);
        dest.merge(source);   
        assertTrue("not covered",dest.getCovered()==0);
    }
    @Test
    public void mergeOneNotVisited() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(true);
        SonarBranchPoint source = new SonarBranchPoint(LINE);
        source.addPath(false);
        dest.merge(source);   
        assertTrue("covered once",dest.getCovered()==1);
    }
    
    @Test
    public void mergeOneBothVisited() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(true);
        SonarBranchPoint source = new SonarBranchPoint(LINE);
        source.addPath(true);
        dest.merge(source);   
        assertTrue("one path covered twice",dest.getCovered()==1);
    }
    
    @Test
    public void mergeTwoBothVisited() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(true);
        dest.addPath(false);
        SonarBranchPoint source = new SonarBranchPoint(LINE);
        source.addPath(false);
        source.addPath(true);
        dest.merge(source);   
        assertTrue("two paths both covered",dest.getCovered()==2);
        assertTrue("two paths to cover",dest.getToCover()==2);
    }
    
    @Test
    public void mergeThreePathsTwoVisited() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(true);
        dest.addPath(false);
        dest.addPath(false);
        SonarBranchPoint source = new SonarBranchPoint(LINE);
        source.addPath(false);
        source.addPath(false);
        source.addPath(true);
        dest.merge(source);   
        assertTrue("three paths two covered",dest.getCovered()==2);
        assertTrue("three paths to cover",dest.getToCover()==3);
    }
    
    @Test
    public void mergeOneAndTwoPathsTwoCovered() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(true);
        dest.addPath(true);
        SonarBranchPoint source = new SonarBranchPoint(LINE);
        source.addPath(false);
        source.addPath(false);
        source.addPath(false);
        dest.merge(source);   
        assertTrue("three paths two covered",dest.getCovered()==2);
        assertTrue("three paths to cover",dest.getToCover()==3);
        
    }
    
    @Test
    public void mergeOneAndTwoPathsOneCovered() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(false);
        dest.addPath(false);
        SonarBranchPoint source = new SonarBranchPoint(LINE);
        source.addPath(false);
        source.addPath(false);
        source.addPath(true);
        dest.merge(source);   
        assertTrue("three paths one covered",dest.getCovered()==1);
        assertTrue("three paths to cover",dest.getToCover()==3);
    }
    
    @Test
    public void mergeTwoAndOneathsOneCovered() {
        SonarBranchPoint dest = new SonarBranchPoint(LINE);
        dest.addPath(false);
        dest.addPath(false);
        SonarBranchPoint source = new SonarBranchPoint(LINE);
        source.addPath(true);
        dest.merge(source);   
        assertTrue("two paths one covered",dest.getCovered()==1);
        assertTrue("two paths to cover",dest.getToCover()==2);
    }
}
