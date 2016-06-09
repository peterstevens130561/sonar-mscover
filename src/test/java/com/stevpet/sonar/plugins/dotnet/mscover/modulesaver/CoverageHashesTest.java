package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class CoverageHashesTest {

    private CoverageHashes hashes = new CoverageHashes() ;
    
    @Test
    public void empyString() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        boolean result=hashes.add("");
        assertFalse(result);
    }
    
    @Test
    public void empyStringTwice() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        hashes.add("");
        boolean result=hashes.add("");
        assertTrue(result);
    }
    
    @Test
    public void shortStringTwice() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        hashes.add("bogus");
        boolean result=hashes.add("bogus");
        assertTrue(result);
    }
    
    @Test
    public void someStrings() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        hashes.add("bogus");
        hashes.add("bloop");
        boolean result=hashes.add("bogus");
        assertTrue(result);
    }
}
