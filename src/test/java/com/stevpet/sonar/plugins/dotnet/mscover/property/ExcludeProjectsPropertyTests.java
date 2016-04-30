package com.stevpet.sonar.plugins.dotnet.mscover.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.ConfigurationPropertyTestsBase;

public class ExcludeProjectsPropertyTests extends ConfigurationPropertyTestsBase<List<String>> {

    @Before
    public void before() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        setup(ExcludeProjectsProperty.class);    
    }
    

    
    @Test
    public void undefinedExpectEmptyArray() {
        List<String> projects = property.getValue();
        assertNotNull("should be list",projects);
        assertEquals("should be empty list",0,projects.size());
    }
    
    @Test
    public void oneElementExpectInArray() {
        this.setPropertyValue("myproject");
        List<String>projects = property.getValue();
        assertEquals("should be list with one element",1,projects.size());
        assertEquals("myproject",projects.get(0));
    }
    
    @Test
    public void twoElementExpectInArray() {
        this.setPropertyValue("myproject,secondproject");
        List<String>projects = property.getValue();
        assertEquals("should be list with one element",2,projects.size());
        assertEquals("myproject",projects.get(0));
        assertEquals("secondproject",projects.get(1));
    }
    
    @Test
    public void twoElementExpectFound() {
        this.setPropertyValue("myproject,secondproject");
        List<String>projects = property.getValue();
        assertTrue(projects.contains("myproject"));
        assertTrue(projects.contains("secondproject"));
        assertFalse(projects.contains("funny"));
    }
}
