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
import org.junit.Before;
import org.junit.Test;


public class UnitTestFileResultModelTest {
    private UnitTestClassResult fileResult;
    
    @Before
    public void before() {
        fileResult = new UnitTestClassResult() ;
    }
    @Test
    public void create() {
        UnitTestClassResult model = new UnitTestClassResult() ;
        Assert.assertNotNull(model);
    }
    
    @Test
    public void addPassedOne_shouldBeOne() {
        addPassedTest();
        Assert.assertEquals(1, (int)fileResult.getPassed());
        Assert.assertEquals(0, (int)fileResult.getFail());
        Assert.assertEquals(1,(int)fileResult.getDensity());
    }

    @Test
    public void addFailedOne_shouldBeOne() {
        addFailedTest();
        Assert.assertEquals(0, (int)fileResult.getPassed());
        Assert.assertEquals(1, (int)fileResult.getFail());
        Assert.assertEquals(0,(int)fileResult.getDensity());        
    }

    @Test
    public void addFailedAndPass_DensityShouldBe50() {

        addFailedTest();
        addPassedTest();
        Assert.assertEquals(1, (int)fileResult.getPassed());
        Assert.assertEquals(1, (int)fileResult.getFail());
        Assert.assertEquals(0.5,fileResult.getDensity(),0.00001);        
    }
    
    private void addFailedTest() {
        UnitTestMethodResult failedTest = new UnitTestMethodResult();
        failedTest.setOutcome("Fail");
        fileResult.add(failedTest);
    }
    private void addPassedTest() {
        UnitTestMethodResult passedTest = new UnitTestMethodResult();
        passedTest.setOutcome("Passed");
        fileResult.add(passedTest);
    }
    
}
