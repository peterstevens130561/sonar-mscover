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

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.property.InvalidPropertyValueException;

public class DefaultIntegrationTestsConfigurationSetttingsTests  {

    private static final String SCHEDULE_PROPERTY_KEY = "sonar.mscover.integrationtests.schedule";
    private Settings settings ;
    private IntegrationTestsConfiguration integrationTestsConfiguration;
    @Mock private FileSystem fileSystem;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        PropertyDefinitions definitions = new PropertyDefinitions(new DefaultIntegrationTestsConfiguration().getProperties());
        settings = new Settings(definitions);
        integrationTestsConfiguration = new DefaultIntegrationTestsConfiguration(settings, fileSystem);
        
    }
    
    @Test
    public void scheduleNotSetExpectDefault() {
        Pattern defaultPattern = integrationTestsConfiguration.getSchedule();
        assertEquals("property not set",".*",defaultPattern.pattern());
    }
    
    @Test
    public void simplePattern() {
        settings.appendProperty(SCHEDULE_PROPERTY_KEY, "[6-7]");
        assertEquals("[6-7]",integrationTestsConfiguration.getSchedule().pattern());
    }
    
    @Test
    public void invalidPattern() {
        settings.appendProperty(SCHEDULE_PROPERTY_KEY, "[6-7");
        try {
            integrationTestsConfiguration.getSchedule().pattern();
        } catch (InvalidPropertyValueException e ) {
            assertEquals(SCHEDULE_PROPERTY_KEY,e.getPropertyKey());
            assertEquals("[6-7",e.getPropertyValue());
            return;
        }
        fail("expected InvalidPropertyException");
    }
}
