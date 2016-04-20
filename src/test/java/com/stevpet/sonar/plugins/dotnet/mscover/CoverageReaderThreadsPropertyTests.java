package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class CoverageReaderThreadsPropertyTests extends ConfigurationPropertyTestsBase<Integer> {
    @Before
    public void before() {
        setup(new CoverageReaderThreadsProperty());
        property = new CoverageReaderThreadsProperty(settings);
        
    }
    @Test
    public void notSpecificedExpectDefault() {
        int value = property.getValue();
        assertEquals("expect default",1,value);
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
    public void tooMuchExpectExecption() {
        setPropertyValue("10");
        try {
            property.validate();
        } catch (InvalidPropertyValueException e) {
            return ; // this is expected
        } catch ( Exception e) {
            fail("just too much expected InvalidPropertyException"); 
        }
    }
    @Test
    public void validLowExpect() {
        setPropertyValue("1");
        int timeout = property.getValue();
        assertEquals(1,timeout);
    }
    
    @Test
    public void validHighExpect() {
        setPropertyValue("9");
        int timeout = property.getValue();
        assertEquals(9,timeout);
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
