package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FilenameFilter;

import org.junit.Test;

public class FakesFilterTest {
    FilenameFilter fakesFilter = new FakesFilter();
    @Test
    public void acceptInvalid_False() {
        assertFalse(fakesFilter.accept(null, "MsCorLib.4.0.0.0.dll"));
    }
    
    @Test
    public void acceptUpperFake_True() {
        assertTrue(fakesFilter.accept(null, "EnvDTE.8.0.0.0.Fakes.dll"));
    }
    
    @Test
    public void acceptLowerFake_True() {
        assertTrue(fakesFilter.accept(null, "EnvDTE.8.0.0.0.fakes.dll"));
    }
    
    @Test
    public void acceptLowerFakeXml_True() {
        assertTrue(fakesFilter.accept(null, "EnvDTE.8.0.0.0.fakes.xml"));
    }
    
    @Test
    public void fakeWithoutDot_False() {
        assertFalse(fakesFilter.accept(null, "fakeAssembly.dll"));       
    }
}
