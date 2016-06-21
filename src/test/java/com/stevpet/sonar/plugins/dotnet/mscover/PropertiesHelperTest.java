/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.SettingsMock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertiesHelperTest {

    private SettingsMock settingsMock = new SettingsMock();
    private Settings settings;
    private MsCoverConfiguration helper;

    @Before
    public void before() {
        settings = mock(Settings.class);
        helper = DefaultMsCoverConfiguration.create(settings);
    }


    @Test
    public void defaultRunVsTest_True() {
        setSetting(DefaultMsCoverConfiguration.MSCOVER_MODE, "runvstest");
        setSetting(DefaultMsCoverConfiguration.MSCOVER_COVERAGETOOL, "vstest");
        boolean actual = helper.runVsTest();
        Assert.assertTrue(actual);
    }

    @Test
    public void otherRunVsTest_False() {
        setSetting(DefaultMsCoverConfiguration.MSCOVER_MODE, "runvstest");
        setSetting(DefaultMsCoverConfiguration.MSCOVER_COVERAGETOOL, "bogus");
        boolean actual = helper.runVsTest();
        Assert.assertFalse(actual);
    }

    @Test
    public void defaultRunOpenCover_False() {
        setSetting(DefaultMsCoverConfiguration.MSCOVER_MODE, "runvstest");
        setSetting(DefaultMsCoverConfiguration.MSCOVER_COVERAGETOOL, "vstest");
        boolean actual = helper.runOpenCover();
        Assert.assertFalse(actual);
    }

    @Test
    public void otherRunOpenCover_False() {
        setSetting(DefaultMsCoverConfiguration.MSCOVER_MODE, "runvstest");
        setSetting(DefaultMsCoverConfiguration.MSCOVER_COVERAGETOOL, "bogus");
        boolean actual = helper.runOpenCover();
        Assert.assertFalse(actual);
    }

    @Test
    public void openCoverRunOpenCover_False() {
        setSetting(DefaultMsCoverConfiguration.MSCOVER_MODE, "runvstest");
        setSetting(DefaultMsCoverConfiguration.MSCOVER_COVERAGETOOL, "opencover");
        boolean actual = helper.runOpenCover();
        Assert.assertTrue(actual);
    }

    @Test
    public void hintPathNotSet_Null() {
        String actual = helper.getUnitTestHintPath();
        assertNull(actual);
    }

    @Test
    public void hintPathSet_Value() {
        String path = "C:/Development/Jewel.Release.Oahu/JewelEarth/bin";
        setSetting(DefaultMsCoverConfiguration.MSCOVER_UNITTEST_HINTPATH, path);
        String actual = helper.getUnitTestHintPath();
        assertEquals(path, actual);

    }

    @Test
    public void hintPathMulti_Ignored() {
        String path = "C:/Development/Jewel.Release.Oahu/JewelEarth/bin,SomeOtherPath";
        setSetting(DefaultMsCoverConfiguration.MSCOVER_UNITTEST_HINTPATH, path);
        String actual = helper.getUnitTestHintPath();
        assertEquals(path, actual);
    }

    
    @Test
    public void noWorkSpaceDefinedExectpion() {
        when(settings.getString("sonar.mscover.workspace")).thenReturn(null);
        try {
            helper.getWorkSpaceRoot();
        } catch (MsCoverException e) {
            return;
        }
        fail("property not defined expect exception");
    }
    
    @Test
    public void workSpaceDoesNotExistDefinedExectpion() {
        when(settings.getString("sonar.mscover.workspace")).thenReturn("Z:/@#$DFGREWSDWS");
        try {
            helper.getWorkSpaceRoot();
        } catch (MsCoverException e) {
            return;
        }
        fail("property not defined expect exception");
    }
    
    @Test
    public void workSpaceDefined() {
        when(settings.getString("sonar.mscover.workspace")).thenReturn("C:/Program Files");
        File workspace= helper.getWorkSpaceRoot();
        assertNotNull("expect valid file",workspace);
        assertEquals("C:\\Program Files",workspace.getAbsolutePath());
    }
    
    private void setSetting(String property, String value) {
        when(settings.getString(property)).thenReturn(value);
    }


}
