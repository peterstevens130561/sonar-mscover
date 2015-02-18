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
package com.stevpet.sonar.plugins.dotnet.mscover.propertieshelper.getlanguages;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetLanguagesTest {
    
    Settings settings;
    MsCoverProperties propertiesHelper;
    private List<String> languages;
    @Before
    public void before() {
        settings=mock(Settings.class);
        when(settings.getStringArrayBySeparator("sonar.language", ",")).thenCallRealMethod();
        propertiesHelper=PropertiesHelper.create(settings);
    }

    @Test
    public void NoLanguageSetting_EmptyList() {
        //Arrange
        //Act
        languages=propertiesHelper.getLanguages();
        //Assert
        assertEquals(0, languages.size());
        
    }
    
    @Test
    public void EmptyLanguageSetting_EmptyList() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("");
        //Act
        languages=propertiesHelper.getLanguages();
        //Assert
        assertEquals(0, languages.size());
        
    }
    
    @Test
    public void OneLanguageSetting_ListHasOne() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("cs");
        //Act
        languages=propertiesHelper.getLanguages();       
        //Assert
        assertEquals(1, languages.size());
        assertEquals("cs",languages.get(0));
    }
    
    @Test
    public void OneLanguageSetting_ListHasTwo() {
        //Arrange
        when(settings.getString("sonar.language")).thenReturn("cs,c++");
        //Act
        languages=propertiesHelper.getLanguages();       
        //Assert
        assertEquals(2, languages.size());
        assertEquals("cs",languages.get(0));
        assertEquals("c++",languages.get(1));
    }
}
