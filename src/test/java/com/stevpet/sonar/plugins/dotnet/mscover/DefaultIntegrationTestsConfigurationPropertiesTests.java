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

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.PropertyDefinition;

public class DefaultIntegrationTestsConfigurationPropertiesTests {

    private DefaultIntegrationTestsConfiguration integrationTestsConfiguration;
    
    @Before
    public void before() {
        integrationTestsConfiguration = new DefaultIntegrationTestsConfiguration();
        
    }
    
    @Test
    public void noDuplicates() {
        Map<String,PropertyDefinition> map = new HashMap<>();
        for(PropertyDefinition propertyDefinition:integrationTestsConfiguration.getProperties()) {
            String key = propertyDefinition.key();
            if(map.containsKey(key)) {
                Assert.fail("duplicate key " + key);
            }
            map.put(key, propertyDefinition);
        }
    }
}
