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
package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.config.Settings;

import static org.mockito.Mockito.when;
public class DEfaultOpenCoverCommandLineConfigurationTests {

    private OpenCoverCommandLineConfiguration configuration ;
    @Mock private Settings settings ;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        configuration = new DefaultOpenCoverCommandLineConfiguration(settings);
    }
    
    @Test
    public void noInstallDirSpecified() {
        try {
            configuration.getInstallDir();
        } catch (IllegalArgumentException e) {
            assertEquals("expected message","property sonar.mscover.opencover.installDirectory not set",e.getMessage());
            return;
        }
        fail("expected message");
    }
    
    @Test
    public void emptyInstallDirSpecified() {
        when(settings.getString("sonar.mscover.opencover.installDirectory")).thenReturn("");
        try {
            configuration.getInstallDir();
        } catch (IllegalArgumentException e) {
            assertEquals("expected message","property sonar.mscover.opencover.installDirectory not set",e.getMessage());
            return;
        }
        fail("expected message");
    }
    @Test
    public void installDirSpecified() {
        String value = "C:/bogus" ;
        when(settings.getString("sonar.mscover.opencover.installDirectory")).thenReturn(value);
        String installDir=configuration.getInstallDir();
        assertEquals(value,installDir);
    }
    
    @Test
    public void givenOpenCoverSkipAutoProps_True() {
        setBoolean(DefaultOpenCoverCommandLineConfiguration.SKIPAUTOPROPS_KEY, true);
        boolean actual = configuration.getSkipAutoProps();
        assertTrue(actual);
    }

    @Test
    public void givenOpenCoverSkipAutoProps_False() {
        setBoolean(DefaultOpenCoverCommandLineConfiguration.SKIPAUTOPROPS_KEY, false);
        boolean actual = configuration.getSkipAutoProps();
        assertFalse(actual);
    }
    
    private void setBoolean(String property, boolean value) {
        when(settings.getBoolean(property)).thenReturn(value);
    }
}
