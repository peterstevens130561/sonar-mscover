/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.testconfigfinder;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetDefaultTest {
    private File solutionFolder ;
    private VsTestConfigFinder configFinder = new VsTestConfigFinder();
    @Before
    public void setUp() throws Exception {
        solutionFolder = mock(File.class);

    }

    @Test
    public void NoFilesFound_NullPath(){
        File files[] = {};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNull(configAbsolutePath);
    }
    
    @Test
    public void TestSettingsFileFound_ExpectPath(){
        String expected = "testSettings.testsettings";
        File files[] = {new File(expected)};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNotNull(configAbsolutePath);
        assertEquals(expected,new File(configAbsolutePath).getName());
    }

    @Test
    public void RunConfigFileFound_ExpectPath(){
        String expected = "testSettings.testrunconfig";
        File files[] = {new File(expected)};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNotNull(configAbsolutePath);
        assertEquals(expected,new File(configAbsolutePath).getName());
    }

    @Test
    public void InvalidConfigFileFound_ExpectNull(){
        String expected = "testrunconfigother";
        File files[] = {new File(expected)};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNull(configAbsolutePath);

    }
    
    @Test
    public void InvalidTestSettingsFound_ExpectNull(){
        String expected = "testsettingsother";
        File files[] = {new File(expected)};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNull(configAbsolutePath);
    }
    
    @Test
    public void SeveralFilesAndConfigFileFound_ExpectConfigFile() {
        String expected = "sonarsettings.testsettings";
        File files[] = {new File("john.sln"),new File(expected),new File("somefile.csproj")};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNotNull(configAbsolutePath);
        assertEquals(expected,new File(configAbsolutePath).getName());      
    }
    
    
    @Test
    public void SeveralFilesAndSeveralConfigFilesFound_ExpectConfigFile() {
        String expected = "sonarsettings.testsettings";
        File files[] = {new File("john.sln"),new File(expected),new File("somefile.csproj"),new File("localsettings.testsettings")};
        when(solutionFolder.listFiles()).thenReturn(files);
        String configAbsolutePath = configFinder.getDefault(solutionFolder);
        assertNotNull(configAbsolutePath);
        assertEquals(expected,new File(configAbsolutePath).getName());      
    }
    
  
}
