package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.property.CoverageReaderThreadsProperty;
import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class CoverageReaderThreadsPropertyTests extends ConfigurationPropertyTestsBase<Integer> {
    @Before
    public void before() {
        setup(new CoverageReaderThreadsProperty());
        property = new CoverageReaderThreadsProperty(settings);
        
    }
    @Test
    public void notSpecificedExpectDefault() {
        checkDefaultOnNotSet(1);
    }
    
    
    @Test
    public void garbagepecificedExpectExecption() {
        checkExceptionOnNotInt();
    }
    
    @Test
    public void negativeSpecificedExpectExecption() {
        checkOutsideRangeInt(-1);
    }
    
    @Test
    public void tooMuchExpectExecption() {
        checkOutsideRangeInt(10);
    }
    @Test
    public void validLowExpect() {
        checkInRangeInt(1);

    }
    
    @Test
    public void validHighExpect() {
        checkInRangeInt(9);
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
