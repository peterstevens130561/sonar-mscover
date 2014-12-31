/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.PatternSyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.SettingsMock;

public class PropertiesHelper_isTestProject_Tests {
    private SettingsMock settingsMock = new SettingsMock();
    private ProjectMock projectMock = new ProjectMock();
    private MsCoverProperties helper;

    @Before
    public void before() {
        helper = PropertiesHelper.create(settingsMock.getMock());
    }
    
    @Test
    public void noPatternDefined_expectFalse() {
        settingsMock.givenString(PropertiesHelper.VISUAL_STUDIO_TEST_PROJECT_PATTERN,null);
        projectMock.givenName("john.csproj");
        
        boolean result=helper.isTestProject(projectMock.getMock());
        
        assertFalse(result);
    }
    
    @Test
    public void illegalPatternDefined_expectException() {
        settingsMock.givenString(PropertiesHelper.VISUAL_STUDIO_TEST_PROJECT_PATTERN,"*UnitTest[");
        projectMock.givenName("john.csproj");
        
        try {
            boolean result=helper.isTestProject(projectMock.getMock());
        } catch (PatternSyntaxException e) {
            return;
        }
        fail("exepcted PatternException");
    }
   
    @Test
    public void patternDefined_expectTrue() {
        settingsMock.givenString(PropertiesHelper.VISUAL_STUDIO_TEST_PROJECT_PATTERN,".*UnitTest.*");
        projectMock.givenName("MyFunny.UnitTest");
        
        boolean result=helper.isTestProject(projectMock.getMock());
        
        assertTrue(result);
    }
    
    @Test
    public void patternDefined_expectFalse() {
        settingsMock.givenString(PropertiesHelper.VISUAL_STUDIO_TEST_PROJECT_PATTERN,".*UnitTest.*");
        projectMock.givenName("MVC");
        
        boolean result=helper.isTestProject(projectMock.getMock());
        
        assertFalse(result);
    }
    
}
