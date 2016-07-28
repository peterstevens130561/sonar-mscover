package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PropertyTest {

    
    @Test
    public void normalProperty() {
        Property property = new Property("key=value");
        assertEquals("key",property.getKey());
        assertEquals("value",property.getValue());
    }
    
    @Test
    public void doubleEqualsProperty() {
        Property property = new Property("key=value=garbage");
        assertEquals("key",property.getKey());
        assertEquals("value=garbage",property.getValue());
    }
    
    @Test
    public void funnyLine() {
        Property property = new Property("ExecutablePath=C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe");
        assertEquals("ExecutablePath",property.getKey());
        assertEquals("C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe",property.getValue());
    }
    
    @Test
    public void invalidLine() {
        try {
        Property property = new Property("MissingSeperator");
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Excpected IllegalArgumentException");
    }
}
