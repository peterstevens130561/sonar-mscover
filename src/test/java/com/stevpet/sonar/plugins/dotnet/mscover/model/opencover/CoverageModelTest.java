package com.stevpet.sonar.plugins.dotnet.mscover.model.opencover;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class CoverageModelTest {

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
