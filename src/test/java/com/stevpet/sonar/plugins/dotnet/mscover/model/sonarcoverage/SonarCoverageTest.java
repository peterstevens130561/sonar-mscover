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
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonarcoverage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class SonarCoverageTest {

    private SonarCoverage model;
    @Before
    public void before() {
         model = new SonarCoverage();       
    }
    @Test 
    public void emptyList_HasZeroItems() {

        Assert.assertEquals(0,model.getValues().size());
    }
    
    
    @Test 
    public void oneItemAdded_ListHasOneItems() {
        SonarFileCoverage fileModel=model.getCoveredFile("0");
        
        Assert.assertEquals(1,model.getValues().size());
        
        Assert.assertNotNull(fileModel);
    }
    
    @Test 
    public void oneItemAddedRequestTwoTimes_ListHasOneItems() {
        SonarFileCoverage fileModel=model.getCoveredFile("0");
        
        fileModel=model.getCoveredFile("0");
        
        Assert.assertNotNull(fileModel);
        Assert.assertEquals(1,model.getValues().size());
    }
    
    @Test
    public void twoItemsAddedRequestItems_MustBeTwoDifferentItems() {
        SonarFileCoverage fileModel0=model.getCoveredFile("0");
        SonarFileCoverage fileModel1=model.getCoveredFile("1");
        Assert.assertNotEquals(fileModel0, fileModel1);
    }
}
