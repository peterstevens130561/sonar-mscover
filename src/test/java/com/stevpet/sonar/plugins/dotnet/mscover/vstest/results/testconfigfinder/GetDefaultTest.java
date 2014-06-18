package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.testconfigfinder;

import static org.junit.Assert.*;

import java.io.File;


import org.junit.Before;
import org.junit.Test;


import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.TestConfigFinder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetDefaultTest {
    private File solutionFolder ;
    @Before
    public void setUp() throws Exception {
        solutionFolder = mock(File.class);
    }

    @Test
    public void NoFilesFound_NullPath(){
        TestConfigFinder configFinder = new TestConfigFinder();
        File files[] = {};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNull(configAbsolutePath);
    }
    
    @Test
    public void TestSettingsFileFound_ExpectPath(){
        TestConfigFinder configFinder = new TestConfigFinder();
        String expected = "testSettings.testsettings";
        File files[] = {new File(expected)};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNotNull(configAbsolutePath);
        assertEquals(expected,new File(configAbsolutePath).getName());
    }

    @Test
    public void RunConfigFileFound_ExpectPath(){
        TestConfigFinder configFinder = new TestConfigFinder();
        String expected = "testSettings.testrunconfig";
        File files[] = {new File(expected)};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNotNull(configAbsolutePath);
        assertEquals(expected,new File(configAbsolutePath).getName());
    }

    @Test
    public void InvalidConfigFileFound_ExpectNull(){
        TestConfigFinder configFinder = new TestConfigFinder();
        String expected = "testrunconfigother";
        File files[] = {new File(expected)};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNull(configAbsolutePath);

    }
    
    @Test
    public void InvalidTestSettingsFound_ExpectNull(){
        TestConfigFinder configFinder = new TestConfigFinder();
        String expected = "testsettingsother";
        File files[] = {new File(expected)};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNull(configAbsolutePath);
    }
    
    @Test
    public void SeveralFilesAndConfigFileFound_ExpectConfigFile() {
        TestConfigFinder configFinder = new TestConfigFinder();
        String expected = "sonarsettings.testsettings";
        File files[] = {new File("john.sln"),new File(expected),new File("somefile.csproj")};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNotNull(configAbsolutePath);
        assertEquals(expected,new File(configAbsolutePath).getName());      
    }
    
    
    @Test
    public void SeveralFilesAndSeveralConfigFilesFound_ExpectConfigFile() {
        TestConfigFinder configFinder = new TestConfigFinder();
        String expected = "sonarsettings.testsettings";
        File files[] = {new File("john.sln"),new File(expected),new File("somefile.csproj"),new File("localsettings.testsettings")};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNotNull(configAbsolutePath);
        assertEquals(expected,new File(configAbsolutePath).getName());      
    }
}
