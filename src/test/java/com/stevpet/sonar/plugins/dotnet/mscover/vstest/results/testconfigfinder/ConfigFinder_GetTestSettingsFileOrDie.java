/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.testconfigfinder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

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
        } catch(IllegalStateException e) {
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
