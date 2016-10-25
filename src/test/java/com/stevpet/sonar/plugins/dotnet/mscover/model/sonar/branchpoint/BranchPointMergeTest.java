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
