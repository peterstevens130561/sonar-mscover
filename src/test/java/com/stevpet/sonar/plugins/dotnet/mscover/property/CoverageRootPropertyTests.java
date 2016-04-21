package com.stevpet.sonar.plugins.dotnet.mscover.property;

import static org.junit.Assert.assertEquals;
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
        File dummy = TestUtils.getResource("CoverageRootProperty/CoverageRoot/dummy");
        File notExistingRoot = new File(dummy.getParentFile(),"bogus/bogus");
        this.setPropertyValue(notExistingRoot.getAbsolutePath());
        super.expectInvalidPropetyValueExceptionOnGetValue();
    }
    
    @Test
    public void existsShouldGet() {
        File dummy = TestUtils.getResource("CoverageRootProperty/CoverageRoot/dummy");
        File existingRoot = dummy.getParentFile();
        this.setPropertyValue(existingRoot.getAbsolutePath());      
        assertEquals("should get path",existingRoot,property.getValue());
    }
}
