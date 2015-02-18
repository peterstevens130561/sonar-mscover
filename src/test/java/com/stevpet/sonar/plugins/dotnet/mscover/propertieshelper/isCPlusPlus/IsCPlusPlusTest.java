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
package com.stevpet.sonar.plugins.dotnet.mscover.propertieshelper.isCPlusPlus;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class IsCPlusPlusTest {
    Settings settings;
    MsCoverProperties propertiesHelper;
    private boolean isCPlusPlus;
    
    @Before
    public void before() {
        settings=mock(Settings.class);
        when(settings.getStringArrayBySeparator("sonar.language", ",")).thenCallRealMethod();
        propertiesHelper=PropertiesHelper.create(settings);
    }

    @Test
    public void noLanguageSetting_EmptyList() {
        //Arrange
        //Act
        isCPlusPlus=propertiesHelper.isCPlusPlus();
        //Assert
        assertFalse(isCPlusPlus);
        
    }
       
    @Test
    public void hasCPlusPlus_ExpectTrue() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("cs,c++");
        //Act
        isCPlusPlus=propertiesHelper.isCPlusPlus();
        //Assert
        assertTrue(isCPlusPlus);
    }
    
    @Test
    public void hasNotCPlusPlus_ExpectFase() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("cs");
        //Act
        isCPlusPlus=propertiesHelper.isCPlusPlus();
        //Assert
        assertFalse(isCPlusPlus);
    }
}
