package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.batch.TimeMachine;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;



public class DateFilterFactoryTest {

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
        TimeMachine timeMachine = mock(TimeMachine.class);
        PropertiesHelper propertiesHelper = mock(PropertiesHelper.class);
        when(propertiesHelper.getCutOffDate()).thenReturn(null);
        
        //Act
        DateFilter filter = DateFilterFactory.createCutOffDateFilter(timeMachine, propertiesHelper);
        //Assert
        Assert.assertTrue(filter.getClass().equals(AlwaysPassThroughDateFilter.class));     
    }
    
    @Test 
    public void createCutOffDateFilterWithFilter_GetCutoff() {
        //Arrange
        TimeMachine timeMachine = mock(TimeMachine.class);
        PropertiesHelper propertiesHelper = mock(PropertiesHelper.class);
        when(propertiesHelper.getCutOffDate()).thenReturn("2014-03-01");
        
        //Act
        DateFilter filter = DateFilterFactory.createCutOffDateFilter(timeMachine, propertiesHelper);
        //Assert
        Assert.assertTrue(filter.getClass().equals(CutOffDateFilter.class));     
    }
}
