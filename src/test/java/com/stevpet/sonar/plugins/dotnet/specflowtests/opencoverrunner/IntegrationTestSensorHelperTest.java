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
package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
public class IntegrationTestSensorHelperTest {

    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private IntegrationTestsConfiguration integrationTestsConfiguration;
    private IntegrationTestSensorHelper integrationTestSensorHelper;
    @Mock private Project project;
    private Pattern pattern = Pattern.compile(".*IntegrationTest.*");
    @Before public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        integrationTestSensorHelper = new IntegrationTestSensorHelper(microsoftWindowsEnvironment,integrationTestsConfiguration);
    }
    
    @Test
    public void noPattern_False() {
        when(integrationTestsConfiguration.getTestProjectPattern()).thenReturn(null);
        assertFalse("no pattern, should not execute",integrationTestSensorHelper.isSolutionWithIntegrationTestProjects(project));     
    }
    
    @Test
    public void pattern_root_False() {  
        when(project.isModule()).thenReturn(false);
        when(integrationTestsConfiguration.getTestProjectPattern()).thenReturn(pattern);
        assertFalse("pattern defined but root should not execute",integrationTestSensorHelper.isSolutionWithIntegrationTestProjects(project));
    }
    
    @Test
    public void pattern_child_trigger() { 
        when(project.isModule()).thenReturn(true);
        when(integrationTestsConfiguration.getTestProjectPattern()).thenReturn(pattern);
        when(microsoftWindowsEnvironment.hasTestProjects(any(Pattern.class))).thenReturn(true);
        assertTrue("pattern and module, testprojects",integrationTestSensorHelper.isSolutionWithIntegrationTestProjects(project));
    }
    
    @Test
    public void pattern_child_noTestProjects() {
        when(project.isModule()).thenReturn(true);
        when(microsoftWindowsEnvironment.hasTestProjects(any(Pattern.class))).thenReturn(false);
        when(integrationTestsConfiguration.getTestProjectPattern()).thenReturn(pattern);
        assertFalse("pattern and module, no testprojects",integrationTestSensorHelper.isSolutionWithIntegrationTestProjects(project));
    }
    
    
 
}
