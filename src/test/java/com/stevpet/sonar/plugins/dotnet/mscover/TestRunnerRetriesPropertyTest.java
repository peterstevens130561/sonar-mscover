package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.config.Settings;

import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class TestRunnerRetriesPropertyTest {
    @Mock private Settings settings ;
    private TestRunnerRetriesProperty property;

    
    @Before()
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        property = new TestRunnerRetriesProperty(settings);
    }
    
    @Test
    public void notANumber() {
        when(settings.getString(property.getKey())).thenReturn("bogus");
        when(settings.getInt(property.getKey())).thenThrow(new NumberFormatException());
        try {
      
            property.onGetValue(settings);
            fail("expected exception");
        } catch (InvalidPropertyValueException e) {
            
        }
    }
    
    @Test
    public void outOfBoundsNumber() {
        when(settings.getString(property.getKey())).thenReturn("-1");
        when(settings.getInt(property.getKey())).thenReturn(-1);
        try {
      
            property.onGetValue(settings);
            fail("expected exception");
        } catch (InvalidPropertyValueException e) {
            
        }
    }
    
    @Test
    public void validNumber() {
        when(settings.getInt(property.getKey())).thenReturn(3);
        int retries=   property.onGetValue(settings);
        assertEquals(3,retries);
    }
}
