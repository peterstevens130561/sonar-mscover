package com.stevpet.sonar.plugins.dotnet.mscover.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class UnitTestFileResultModelTest {
    private UnitTestFileResultModel fileResult;
    
    @Before
    public void before() {
        fileResult = new UnitTestFileResultModel() ;
    }
    @Test
    public void create() {
        UnitTestFileResultModel model = new UnitTestFileResultModel() ;
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
        UnitTestResultModel failedTest = new UnitTestResultModel();
        failedTest.setOutcome("Fail");
        fileResult.add(failedTest);
    }
    private void addPassedTest() {
        UnitTestResultModel passedTest = new UnitTestResultModel();
        passedTest.setOutcome("Passed");
        fileResult.add(passedTest);
    }
    
}
