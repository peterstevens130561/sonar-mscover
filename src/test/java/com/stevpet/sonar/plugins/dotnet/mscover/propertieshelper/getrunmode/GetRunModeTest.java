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
package com.stevpet.sonar.plugins.dotnet.mscover.propertieshelper.getrunmode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.DefaultMsCoverConfiguration.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;

import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.when ;

public class GetRunModeTest {
    
    private Settings settings;
    private MsCoverConfiguration propertiesHelper ;
    @Before
    public void before() {
        settings = mock(Settings.class);
        propertiesHelper = DefaultMsCoverConfiguration.create(settings);
    }
    
    @Test
    public void settingNotSpecified_Skip() {
        test(RunMode.SKIP,null);
    }
    
    @Test
    public void settingEmpty_Skip() {
        test(RunMode.SKIP,"");
    }
    
    @Test(expected=IllegalStateException.class)
    public void settingInvalid_Exception() {
        test(RunMode.SKIP,"booh");
    }
 
    @Test
    public void settingMixedCaseReuse_REUSE() {
        test(RunMode.REUSE,"rEuSe");
    }
    
    @Test
    public void settingRunVsTest_RUNVSTEST() {
        //Arrange
        test(RunMode.RUNVSTEST,"runvstest");
    }
    
    @Test
    public void settingSkipt_SKIP() {
        test(RunMode.SKIP,"skip");
    }
    
    private void test(RunMode expected,String propertyValue) {
        when(settings.getString(DefaultMsCoverConfiguration.MSCOVER_MODE)).thenReturn(propertyValue);       
        RunMode runMode=propertiesHelper.getRunMode();
        //Assert
        Assert.assertEquals(expected, runMode);
    }
}
