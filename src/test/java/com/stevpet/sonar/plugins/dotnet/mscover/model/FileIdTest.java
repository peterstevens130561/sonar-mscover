package com.stevpet.sonar.plugins.dotnet.mscover.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class FileIdTest {
    
    @Test
    public void shouldNotCreateFileIdWithNull() {
        try {
            new FileId(null);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("should have failed with IllegalArgumentException");
    }
    @Test
    public void sameWithEqualsMethod() {
        FileId first = new FileId("bogus");
        FileId second = new FileId("bogus");
        assertTrue("should be same",first.equals(second));
    }
    
    @Test
    public void sameWithEqualsSign() {
        FileId first = new FileId("bogus");
        FileId second = new FileId("bogus");
        assertTrue("should not be the same",first != second);
    }
    
    @Test
    public void notsame() {
        FileId first = new FileId("bogus");
        FileId second = new FileId("other");
        assertTrue("should be different",first != second);
    }
}
