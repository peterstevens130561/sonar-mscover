package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import org.junit.Assert;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;



public class DateFilterFactoryTest {
    private MsCoverPropertiesMock propertiesMock = new MsCoverPropertiesMock();
    private TimeMachineMock timeMachineMock = new TimeMachineMock();
    @Test
    public void createEmptyFilter_ShouldExist() {
        //Arrange
        //Act
        DateFilter emptyFilter = DateFilterFactory.createEmptyDateFilter();
        //Assert
        Assert.assertNotNull(emptyFilter);
        Assert.assertTrue(emptyFilter.isResourceIncludedInResults(null));
    }
    
    @Test 
    public void createCutOffDateFilterNoDate_AlwaysPassThrough() {
        //Arrange

        propertiesMock.givenCutOffDate(null);     
        //Act
        DateFilter filter = DateFilterFactory.createCutOffDateFilter(timeMachineMock.getMock(), propertiesMock.getMock());
        //Assert
        Assert.assertTrue(filter.getClass().equals(AlwaysPassThroughDateFilter.class));     
    }
    
    @Test 
    public void createCutOffDateFilterWithFilter_GetCutoff() {
        //Arrange
        propertiesMock.givenCutOffDate("2014-03-01");      
        //Act
        DateFilter filter = DateFilterFactory.createCutOffDateFilter(timeMachineMock.getMock(), propertiesMock.getMock());
        //Assert
        Assert.assertTrue(filter.getClass().equals(CutOffDateFilter.class));     
    }
}
