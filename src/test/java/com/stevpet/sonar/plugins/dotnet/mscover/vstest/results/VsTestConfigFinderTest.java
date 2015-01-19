package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;

public class VsTestConfigFinderTest {
    private VsTestConfigFinder finder = new VsTestConfigFinder();
    private String ROOT = "VsTestConfigFinder";
    private File solutionDir;

    @Test
    public void test() {
        solutionDir = TestUtils.getResource(ROOT + "/default");

        File settingsFile=finder.getTestSettingsFileOrDie(solutionDir,null);
        assertNotNull(settingsFile);
        assertEquals("default.testrunconfig",settingsFile.getName());
    }
    
    @Test
    public void inProject_expectConfigInSolution() {
        solutionDir = TestUtils.getResource(ROOT + "\\default");

        File settingsFile=finder.getTestSettingsFileOrDie(solutionDir,null);
        assertNotNull(settingsFile);
        assertEquals("default.testrunconfig",settingsFile.getName());
        assertEquals(solutionDir.getAbsolutePath(),settingsFile.getParent());
    }

    @Test
    public void notProject_expectException() {

        solutionDir = TestUtils.getResource(ROOT + "\\notInProject_expectException");

        try {
            finder.getTestSettingsFileOrDie(solutionDir,null);
        } catch (SonarException e ) {
            assertEquals("sonar.mscover.vstest.testsettings not set, and no testsettings file found",e.getMessage());
            return;
        }
        fail("expected exception");
    }
    
    @Test
    public void setInProject_expectFile() {
        solutionDir = TestUtils.getResource(ROOT + "\\InProject_ExpectFile");
        File settingsFile=finder.getTestSettingsFileOrDie(solutionDir,"mystic.conftxt");
        assertNotNull(settingsFile);
        assertEquals("mystic.conftxt",settingsFile.getName());
        assertEquals(solutionDir.getAbsolutePath().toUpperCase(),settingsFile.getParentFile().getAbsolutePath().toUpperCase());
    }
    
    @Test
    public void setInProject_relativeInRoot() {
        solutionDir = TestUtils.getResource(ROOT + "\\relative\\solutions\\solution");
        File settingsFile=finder.getTestSettingsFileOrDie(solutionDir,"../../intop.cfg");
        assertNotNull(settingsFile);
        assertEquals("intop.cfg",settingsFile.getName());
        File expectedDir=TestUtils.getResource(ROOT + "\\relative");
        assertEquals(expectedDir.getAbsolutePath().toUpperCase(),settingsFile.getParentFile().getAbsolutePath().toUpperCase());
    }
    @Test
    public void setInProject_notThere_expectException() {

        solutionDir = TestUtils.getResource(ROOT + "\\InProject_ExpectFile");
        try {
            finder.getTestSettingsFileOrDie(solutionDir,"bmyst.conftxt");
        } catch (SonarException e ) {
            assertEquals("sonar.mscover.vstest.testsettings=bmyst.conftxt not found",e.getMessage());
            return;
        }
        fail("expected exception");
    }
}
