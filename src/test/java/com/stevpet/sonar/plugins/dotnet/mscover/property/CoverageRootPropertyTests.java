package com.stevpet.sonar.plugins.dotnet.mscover.property;

import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.ConfigurationPropertyTestsBase;

public class CoverageRootPropertyTests extends ConfigurationPropertyTestsBase<File>{

    @Before()
    public void before() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        setup(CoverageRootProperty.class);    
    }
    
    @Test
    public void undefinedExpectException() {
        try {
            property.getValue();
        } catch ( InvalidPropertyValueException e ) {
            return ;
        }
        fail("property not defined, expect exception");
    }
    
    @Test
    public void noParentExpectException() {
        this.setPropertyValue("dlddldl");
        super.expectInvalidPropetyValueExceptionOnGetValue();
    }
    
    @Test
    public void doesNotExisttException() {
        File doesNotExist = TestUtils.getResource("CoverageRootProperty/CoverageRoot");
        this.setPropertyValue(doesNotExist.getAbsolutePath());
        super.expectInvalidPropetyValueExceptionOnGetValue();
    }
}
