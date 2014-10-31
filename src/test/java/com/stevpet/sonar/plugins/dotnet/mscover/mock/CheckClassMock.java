package com.stevpet.sonar.plugins.dotnet.mscover.mock;

import static org.junit.Assert.*;

import org.junit.Test;

public class CheckClassMock {
    
    @Test
    public void testMock() {
        SomeClassMock someClassMock = new SomeClassMock();
        SomeClass mock=someClassMock.getMock();
        assertNotNull(mock);
    }

    private class SomeClassMock extends GenericClassMock<SomeClass> {

        public SomeClassMock() {
            super(SomeClass.class);
        }
        
    }
    private class SomeClass {
        
    }
}
