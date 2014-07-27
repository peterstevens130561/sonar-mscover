package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

public class VsTestConfigFinderTest {

    private String ROOT = "VsTestConfigFinder";
    @Test
    public void test() {
        File solutionDir = TestUtils.getResource(ROOT + "/default");
        VsTestConfigFinder finder = new VsTestConfigFinder(solutionDir);
        File settingsFile=finder.getTestSettingsFileOrDie(null);
        assertNotNull(settingsFile);
        assertEquals("default.testrunconfig",settingsFile.getName());
    }
    
    @Test
    public void inProject_expectConfigInSolution() {
        File projectDir = TestUtils.getResource(ROOT + "\\default\\project");
        File solutionDir = TestUtils.getResource(ROOT + "\\default");
        VsTestConfigFinder finder = new VsTestConfigFinder(projectDir);
        File settingsFile=finder.getTestSettingsFileOrDie(null);
        assertNotNull(settingsFile);
        assertEquals("default.testrunconfig",settingsFile.getName());
        assertEquals(solutionDir.getAbsolutePath(),settingsFile.getParent());
    }

    @Test
    public void notProject_expectException() {

        File solutionDir = TestUtils.getResource(ROOT + "\\notInProject_expectException");
        VsTestConfigFinder finder = new VsTestConfigFinder(solutionDir);
        File settingsFile=null;
        try {
            settingsFile=finder.getTestSettingsFileOrDie(null);
        } catch (SonarException e ) {
            assertEquals("sonar.mscover.vstest.testsettings not set, and no testsettings file found",e.getMessage());
            return;
        }
        fail("expected exception");
    }
    
    @Test
    public void setInProject_expectFile() {

        File solutionDir = TestUtils.getResource(ROOT + "\\InProject_ExpectFile");
        VsTestConfigFinder finder = new VsTestConfigFinder(solutionDir);
        File settingsFile=finder.getTestSettingsFileOrDie("mystic.conftxt");
        assertNotNull(settingsFile);
        assertEquals("mystic.conftxt",settingsFile.getName());
        assertEquals(solutionDir.getAbsolutePath(),settingsFile.getParent());
    }
    
    @Test
    public void setInProject_relativeInRoot() {
        File solutionDir = TestUtils.getResource(ROOT + "\\relative\\solutions\\solution");
        VsTestConfigFinder finder = new VsTestConfigFinder(solutionDir);
        File settingsFile=finder.getTestSettingsFileOrDie("../../intop.cfg");
        assertNotNull(settingsFile);
        assertEquals("intop.cfg",settingsFile.getName());
        File expectedDir=TestUtils.getResource(ROOT + "\\relative");
        assertEquals(expectedDir.getAbsolutePath(),settingsFile.getParent());
    }
    @Test
    public void setInProject_notThere_expectException() {

        File solutionDir = TestUtils.getResource(ROOT + "\\InProject_ExpectFile");
        VsTestConfigFinder finder = new VsTestConfigFinder(solutionDir);
        File settingsFile=null;
        try {
            settingsFile=finder.getTestSettingsFileOrDie("bmyst.conftxt");
        } catch (SonarException e ) {
            assertEquals("sonar.mscover.vstest.testsettings=bmyst.conftxt not found",e.getMessage());
            return;
        }
        fail("expected exception");
    }
}
