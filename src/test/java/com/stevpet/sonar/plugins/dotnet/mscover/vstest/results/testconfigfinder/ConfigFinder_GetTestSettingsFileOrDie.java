package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.testconfigfinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(VsTestConfigFinder.class)
public class ConfigFinder_GetTestSettingsFileOrDie {
    private VsTestConfigFinder configFinder = new VsTestConfigFinder();
    
    @Before
    public void setUp() throws Exception {

    }
    
    @Test
    public void NotExistingFile_ExpectException() {
        File solutionDir=selectTestDir("NoFile");
        String settings="unittest.runsettings";
        File configAbsolutePath ;
        try {
            configAbsolutePath = configFinder.getTestSettingsFileOrDie(solutionDir, settings);
        } catch(SonarException e) {
            return;
        }
        assertNull("should not find any path",configAbsolutePath);
   
    }
    
    @Test
    public void ExistingFile_ExpectFound() {
        File solutionDir=selectTestDir("InDir");
        String settings="unittest.runsettings";
        File configAbsolutePath = configFinder.getTestSettingsFileOrDie(solutionDir, settings);
        assertNotNull("expect settings file found",configAbsolutePath);
        assertTrue("settings file should be in InDir",configAbsolutePath.getAbsolutePath().endsWith("InDir\\" + settings));
   
    }
    
    @Test
    public void ExistingFile_ExpectFoundInTestDir() {
        File solutionDir=selectTestDir("InSub");
        String settings="tools\\unittest.runsettings";
        File configAbsolutePath = configFinder.getTestSettingsFileOrDie(solutionDir, settings);
        assertNotNull("expect settings file found",configAbsolutePath);
        assertTrue("settings file should be in InSubDir/tools but is" + configAbsolutePath.getAbsolutePath(),configAbsolutePath.getAbsolutePath().endsWith("InSub\\" + settings));
   
    }
    
    private File selectTestDir(String testDirName) {
        File solutionFile = TestUtils.getResource("ConfigFinder\\" + testDirName + "\\SolutionDir\\solution.sln");
        assertNotNull("did not find solution file for "+ testDirName,solutionFile);
        return solutionFile.getParentFile();
        
    }
}
