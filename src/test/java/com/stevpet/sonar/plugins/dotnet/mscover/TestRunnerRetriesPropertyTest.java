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
package com.stevpet.sonar.plugins.dotnet.mscover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.config.Settings;

import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class TestRunnerRetriesPropertyTest {
    @Mock private Settings settings ;
    private TestRunnerRetriesProperty property;

    
    @Before()
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        property = new TestRunnerRetriesProperty(settings);
    }
    
    @Test
    public void notANumber() {
        when(settings.getString(property.getKey())).thenReturn("bogus");
        when(settings.getInt(property.getKey())).thenThrow(new NumberFormatException());
        try {
      
            property.onGetValue(settings);
            fail("expected exception");
        } catch (InvalidPropertyValueException e) {
            
        }
    }
    
    @Test
    public void outOfBoundsNumber() {
        when(settings.getString(property.getKey())).thenReturn("-1");
        when(settings.getInt(property.getKey())).thenReturn(-1);
        try {
      
            property.onGetValue(settings);
            fail("expected exception");
        } catch (InvalidPropertyValueException e) {
            
        }
    }
    
    @Test
    public void validNumber() {
        when(settings.getInt(property.getKey())).thenReturn(3);
        int retries=   property.onGetValue(settings);
        assertEquals(3,retries);
    }
}
