package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;


import com.stevpet.sonar.plugins.dotnet.mscover.property.TestRunnerTimeoutProperty;

public class TestRunnerTimeoutPropertyTests extends ConfigurationPropertyTestsBase<Integer> {
    

    @Before
    public void before() {
        setup(new TestRunnerTimeoutProperty());
        property = new TestRunnerTimeoutProperty(settings);
        
    }
    @Test
    public void notSpecificedExpectDefault() {
        int value = property.getValue();
        assertEquals("expect default",120,value);
    }
    
    
    @Test
    public void garbagepecificedExpectExecption() {
        setPropertyValue("groble");
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return ; // this is expected
        } catch ( Exception e) {
            fail("expected InvalidPropertyException"); 
        }
    }
    
    @Test
    public void negativeSpecificedExpectExecption() {
        setPropertyValue("-1");
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return ; // this is expected
        } catch ( Exception e) {
            fail("expected InvalidPropertyException"); 
        }
    }
    @Test
    public void validExpect() {
        setPropertyValue("5");
        int timeout = property.getValue();
        assertEquals(5,timeout);
    }
    @Test
    public void notSpecifiedShouldValidate() {
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            fail("should not have InvalidPropertyValueException, as setting is not required");
        } catch ( Exception e) {
            fail("no exception expected"); 
        }
        // fine
    }
    
}
