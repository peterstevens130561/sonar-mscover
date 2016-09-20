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
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonarcoverage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class SonarCoverageTest {

    private ProjectCoverageRepository model;
    @Before
    public void before() {
         model = new DefaultProjectCoverageRepository();       
    }
    @Test 
    public void emptyList_HasZeroItems() {

        Assert.assertEquals(0,model.getValues().size());
    }
    
    
    @Test 
    public void oneItemAdded_ListHasOneItems() {
        SonarFileCoverage fileModel=model.getCoverageOfFile("0");
        
        Assert.assertEquals(1,model.getValues().size());
        
        Assert.assertNotNull(fileModel);
    }
    
    @Test 
    public void oneItemAddedRequestTwoTimes_ListHasOneItems() {
        SonarFileCoverage fileModel=model.getCoverageOfFile("0");
        
        fileModel=model.getCoverageOfFile("0");
        
        Assert.assertNotNull(fileModel);
        Assert.assertEquals(1,model.getValues().size());
    }
    
    @Test
    public void twoItemsAddedRequestItems_MustBeTwoDifferentItems() {
        SonarFileCoverage fileModel0=model.getCoverageOfFile("0");
        SonarFileCoverage fileModel1=model.getCoverageOfFile("1");
        Assert.assertEquals("items have same state",fileModel0, fileModel1);
        Assert.assertNotSame("items are different instances",fileModel0,fileModel1);
    }
    
    @Test
    public void twoItemsAddedRequestItems_GiveDifferentSate_ShouldBeUnEqual() {
        SonarFileCoverage fileModel0=model.getCoverageOfFile("0");
        SonarFileCoverage fileModel1=model.getCoverageOfFile("1");
        fileModel0.setAbsolutePath("a");
        fileModel1.setAbsolutePath("b");
        Assert.assertNotEquals("items have differentstate",fileModel0, fileModel1);

    }
    
    @Test
    public void twoItemsAddedRequestItems_GiveDifferentBranchPoint_ShouldBeUnEqual() {
        SonarFileCoverage fileModel0=model.getCoverageOfFile("0");
        SonarFileCoverage fileModel1=model.getCoverageOfFile("1");
        fileModel0.setAbsolutePath("a");
        fileModel1.setAbsolutePath("a");
        fileModel0.addBranchPoint(10, false);
        fileModel1.addBranchPoint(5,false);
        Assert.assertNotEquals("items have differentstate",fileModel0, fileModel1);
    }
    
    @Test
    public void twoItemsAddedRequestItems_GiveDifferentLinePoint_ShouldBeUnEqual() {
        SonarFileCoverage fileModel0=model.getCoverageOfFile("0");
        SonarFileCoverage fileModel1=model.getCoverageOfFile("1");
        fileModel0.setAbsolutePath("a");
        fileModel1.setAbsolutePath("a");
        fileModel0.addLinePoint(10, false);
        fileModel1.addLinePoint(5,false);
        Assert.assertNotEquals("items have differentstate",fileModel0, fileModel1);
    }
    
    @Test
    public void twoItemsAddedRequestItems_GiveSameData_ShouldBeEqual() {
        SonarFileCoverage fileModel0=model.getCoverageOfFile("0");
        SonarFileCoverage fileModel1=model.getCoverageOfFile("1");
        fileModel0.setAbsolutePath("a");
        fileModel1.setAbsolutePath("a");
        fileModel0.addLinePoint(10, false);
        fileModel1.addLinePoint(10,false);
        fileModel0.addBranchPoint(10, true);
        fileModel1.addBranchPoint(10,true);
        Assert.assertEquals("items have same state",fileModel0, fileModel1);
        Assert.assertNotSame("items are different instances",fileModel0,fileModel1);
    }
}
