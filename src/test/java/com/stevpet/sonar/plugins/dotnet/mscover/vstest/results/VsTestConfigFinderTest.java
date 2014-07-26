package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.sonar.test.TestUtils;

public class VsTestConfigFinderTest {

    @Test
    public void test() {
        File solutionDir = TestUtils.getResource("VsTestConfigFinder/default");
        TestConfigFinder finder = new VsTestConfigFinder(solutionDir);
        finder.getTestSettingsFileOrDie(null);
    }

}
