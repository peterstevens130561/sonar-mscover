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
        FileId first = new FileId("1");
        FileId second = new FileId("1");
        assertTrue("should be same",first.equals(second));
    }
    
    @Test
    public void sameWithEqualsSign() {
        FileId first = new FileId("1");
        FileId second = new FileId("1");
        assertTrue("should not be the same",first != second);
    }
    
    @Test
    public void notsame() {
        FileId first = new FileId("1");
        FileId second = new FileId("2");
        assertTrue("should be different",first != second);
    }
    
    @Test
    public void twoGreaterThanOne() {
        FileId first = new FileId("1");
        FileId second = new FileId("2");
        assertTrue("2 should be greater than 1",first.compareTo(second)<0);
    }
    
    @Test
    public void oneLessThanTwo() {
        FileId first = new FileId("1");
        FileId second = new FileId("2");
        assertTrue("2 should be greater than 1",second.compareTo(first)>0);
    }
    
    @Test
    public void cloneShouldBeEqual() {
        FileId mother = new FileId("34681");
        FileId clone = (FileId)mother.clone();
        assertTrue("should be same",clone.equals(mother));
    }
}
