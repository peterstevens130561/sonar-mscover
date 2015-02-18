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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SonarMsCoverPropertiesLogicTest {
    MsCoverPropertiesStub properties = new MsCoverPropertiesStub();
    MsCoverPropertiesLogic logic = new SonarMsCoverPropertiesLogic(properties);
    @Before
    public void before() {
        
    }
    @Test
    public void createPropertiesLogic_ShouldHaveInstance() {
        assertNotNull(logic);
        boolean actual = logic.isIntegrationTestsEnabled();
        assertFalse(actual);
    }
    
    @Test
    public void integrationTestsPathDefined_IntegrationTestsEnabled() {
        String integrationTestsPath = "aap";
        properties.setIntegrationTestsPath(integrationTestsPath);
        boolean actual=logic.isIntegrationTestsEnabled();
        assertTrue(actual);
    }
    
    @Test
    public void unitTestsPathNotDefined_UnitTestsNotEnabled() {
        String unitTestCoveragePath="";
        properties.setUnitTestCoveragePath(unitTestCoveragePath);
        boolean actual=logic.isUnitTestsEnabled();
        assertFalse(actual);
    }
    @Test
    public void unitTestsPathDefined_UnitTestsEnabled() {
        String unitTestCoveragePath="aap";
        properties.setUnitTestCoveragePath(unitTestCoveragePath);
        boolean actual=logic.isUnitTestsEnabled();
        assertTrue(actual);
    }
}
