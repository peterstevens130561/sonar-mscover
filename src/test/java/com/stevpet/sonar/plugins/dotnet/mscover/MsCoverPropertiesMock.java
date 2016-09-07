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
package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;

import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.when;

public class MsCoverPropertiesMock {
    private MsCoverConfiguration properties = mock(MsCoverConfiguration.class);
    
    public MsCoverConfiguration getMock() {
        return properties;
    }


    public void givenUnitTestHintPath(String hintPath) {
        when(properties.getUnitTestHintPath()).thenReturn(hintPath);
    }
    
    public void givenUnitTestAssembliesThatCanBeIgnoredIfMissing(Collection<String> list) {
        when(properties.getUnitTestAssembliesThatCanBeIgnoredIfMissing()).thenReturn(list);
    }

    public void givenRequiredBuildPlatform(String buildPlatform) {
        when(properties.getRequiredBuildPlatform()).thenReturn(buildPlatform);
    }

    public void givenRequiredBuildConfiguration(String buildConfiguration) {
        when(properties.getRequiredBuildConfiguration()).thenReturn(buildConfiguration);
    }
    
    /**
     * 
     * @param mode - to return when getMode is used
     */
    public void givenMode(String mode) {
        when(properties.getMode()).thenReturn(mode);
    }


 }
