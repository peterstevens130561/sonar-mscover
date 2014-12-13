package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Test;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


public class RemoveFakesTest {
    FakesRemover fakesRemover = new DefaultFakesRemover();

    @Test
    public void NoFakesInUnitTestDir_Removed() {
        File testDir=TestUtils.getResource("FakesRemover/NoFile");
        fakesRemover.removeFakes(testDir);
        File noFile = TestUtils.getResource("FakesRemover/NoFile/file.dll");
        assertTrue(noFile.exists());
    }
    
    @Test
    public void FakesInUnitTestDir_Removed() {
        String root = "FakesRemover/SomeFiles";
        File testDir=TestUtils.getResource(root);
        int removed=fakesRemover.removeFakes(testDir);
        assertEquals(2,removed);
    }

    
    @Test
    public void FakesNoTestDir_Return() {
        fakesRemover.removeFakes(null);        
    }
    
    @Test
    public void FakesNoFiles_Return() {
        File emptyDir = mock(File.class);
        when(emptyDir.list(any(FilenameFilter.class))).thenReturn(null);
        fakesRemover.removeFakes(emptyDir);
        
    }

}