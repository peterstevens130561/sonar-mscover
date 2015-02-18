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
package com.stevpet.sonar.plugins.dotnet.mscover.propertieshelper.getignored;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;


public class GetIgnoredTest {
    String[] emptyList = {};
    String[] listWithOne = { "one" };
    String[] listWithTwo = { "one","two" };
    
    Settings settings = mock(Settings.class);
    MsCoverProperties propertiesHelper = PropertiesHelper.create(settings);
    @Before
    public void before() {
        
    }
    
    @Test
    public void notSpecified_EmptyList() {
        mockSettingsThenExpect(emptyList,0);
    }
    
    @Test
    public void oneSpecified_ListWithOne() {
        mockSettingsThenExpect(listWithOne,1);
    }
    @Test
    public void twoSpecified_ListWithTwo() {
        mockSettingsThenExpect(listWithTwo,2);
    }
    
    
    private void mockSettingsThenExpect(String []list,int size) {
        when(settings.getStringArrayBySeparator(PropertiesHelper.MSCOVER_IGNOREMISSING_DLL,",")).thenReturn(list);

        Collection<String> ignoreMissing = propertiesHelper.getUnitTestAssembliesThatCanBeIgnoredIfMissing();
        assertNotNull(ignoreMissing);
        assertEquals(size,ignoreMissing.size());      
    }
}
