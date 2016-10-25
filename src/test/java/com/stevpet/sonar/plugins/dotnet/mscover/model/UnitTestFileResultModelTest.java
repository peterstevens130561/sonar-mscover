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
package com.stevpet.sonar.plugins.dotnet.mscover.model;

import java.io.File;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class UnitTestFileResultModelTest {
    private ClassUnitTestResult fileResult;
    
    @Before
    public void before() {
    	
        fileResult = new ClassUnitTestResult(new File("bogus")) ;
    }
    @Test
    public void create() {
        Assert.assertNotNull(fileResult);
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
    public void addIgnoredOne_shouldBeOne() {
        addIgnoredTest();
        Assert.assertEquals("no test to pass", 0,(int)fileResult.getPassed());
        Assert.assertEquals("one ignored test",1, (int)fileResult.getIgnored());
        Assert.assertEquals("as there is no test to pass, density is 100%",1,(int)fileResult.getDensity());        
    }
    
    @Test
    public void addOneIgnoredOnePassed_shouldBe100() {
        addIgnoredTest();
        addPassedTest();
        Assert.assertEquals("one passed", 1,(int)fileResult.getPassed());
        Assert.assertEquals("one ignored test",1, (int)fileResult.getIgnored());
        Assert.assertEquals("as there is one test to pass, which passed, density is 100%",1,(int)fileResult.getDensity());        
    }
    
    @Test
    public void addOneIgnoredOnePassedOneFaiiled_shouldBe50() {
        addIgnoredTest();
        addPassedTest();
        addFailedTest();
        Assert.assertEquals("one passed", 1,(int)fileResult.getPassed());
        Assert.assertEquals("one ignored test",1, (int)fileResult.getIgnored());
        
        Assert.assertEquals("as there is one test to pass, and one failed, density is 50%",0.5,fileResult.getDensity(),0.0001);        
    }
    @Test
    public void addFailedAndPass_DensityShouldBe50() {

        addFailedTest();
        addPassedTest();
        Assert.assertEquals(1, (int)fileResult.getPassed());
        Assert.assertEquals(1, (int)fileResult.getFail());
        Assert.assertEquals(0.5,fileResult.getDensity(),0.00001);        
    }
    
    @Test
    public void checkDuration() {
        UnitTestMethodResult test = newUnitTestMethod();
        LocalTime localTime = LocalTime.of(0, 3, 10, 11150000);
        test.setTime(localTime).setOutcome("Failed");
        fileResult.add(test);
        Assert.assertEquals((Double)((3*60+10)*1000.0 + 11),fileResult.getLocalTimeMillis());       
    }
    private UnitTestMethodResult newUnitTestMethod() {
        return new UnitTestMethodResult("SOMEID");
    }
    
    @Test
    public void checkDurationAdd() {
        UnitTestMethodResult test = newUnitTestMethod();
        LocalTime localTime = LocalTime.of(0, 3, 10, 11150000);
        test.setTime(localTime).setOutcome("Failed");
        fileResult.add(test);
        
        test = newUnitTestMethod();
        localTime = LocalTime.of(1, 1, 10, 0);
        test.setTime(localTime).setOutcome("Failed");
        fileResult.add(test);
        Assert.assertEquals((Double)((3*60+10 + 3600 + 60 + 10)*1000.0 + 11 ),fileResult.getLocalTimeMillis());
        
    }
    private void addFailedTest() {
        UnitTestMethodResult failedTest = newUnitTestMethod();
        failedTest.setOutcome("Failed");
        fileResult.add(failedTest);
    }
    private void addPassedTest() {
        UnitTestMethodResult passedTest = newUnitTestMethod();
        passedTest.setOutcome("Passed");
        fileResult.add(passedTest);
    }
    
    private void addIgnoredTest() {
        UnitTestMethodResult ignoredTest = newUnitTestMethod();
        ignoredTest.setOutcome("NotExecuted");
        fileResult.add(ignoredTest);
    }
    
}
