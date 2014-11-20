package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Test;
import org.sonar.test.TestUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.Assert.assertTrue;

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
        fakesRemover.removeFakes(testDir);
        expectExists(root,"cfake.txt");
        expectExists(root,"valid.cs");
        expectDoesNotExist(root,"fakea.dll");
        expectDoesNotExist(root,"fakeb.xml");
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
    private void expectExists(String root,String name) {
        File noFile = TestUtils.getResource(root + "/" + name);
        assertTrue(name,noFile.exists());
    }
    
    private void expectDoesNotExist(String root,String name) {
        File noFile = TestUtils.getResource(root + "/" + name);
        assertEquals(name,false,noFile.exists());
    }

    private void assertEquals(String name, boolean b, boolean exists) {
        // TODO Auto-generated method stub
        
    }

}
