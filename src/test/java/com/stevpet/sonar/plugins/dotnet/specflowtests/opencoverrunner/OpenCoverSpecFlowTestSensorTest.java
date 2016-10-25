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
package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Mode;
import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration.Tool;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

import static org.mockito.Mockito.when;

public class OpenCoverSpecFlowTestSensorTest {

    private Sensor sensor ;
    @Mock private Project project;
    @Mock private IntegrationTestsConfiguration configuration;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private SensorContext context;
    @Mock private IntegrationTestRunnerApplication integrationTestRunner ;
    @Mock private OpenCoverSpecFlowTestSaverSensor saverSensor ;
    @Mock private VsTestTestResultsSaver testResultsSaver;
    private ProjectUnitTestResults projectUnitTestResults;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        sensor = new OpenCoverSpecFlowTestSensor(configuration, integrationTestRunner, saverSensor, testResultsSaver);
        Pattern specflowPattern = Pattern.compile(".*SpecFlow.*");
        when(configuration.getTestProjectPattern()).thenReturn(specflowPattern);
        when(project.isModule()).thenReturn(true);
        when(configuration.matches(Tool.OPENCOVER, Mode.ACTIVE)).thenReturn(true);
    }
    
    @Test
    public void shouldSkipNormalProject() {
        when(project.getName()).thenReturn("someproject");
        when(project.isModule()).thenReturn(true);
        assertTrue("is a normal project, so execute",sensor.shouldExecuteOnProject(project));      
    }
    
    @Test
    public void souldSkipRootProject() {
        when(project.getName()).thenReturn("SpecFlow");
        when(project.isModule()).thenReturn(false);
        assertFalse("name matches pattern, but is root, so skip",sensor.shouldExecuteOnProject(project));       
    }
    
    @Test
    public void shouldRunonSpecFlowProject() {
        when(project.getName()).thenReturn("SpecFlow");
        when(project.isModule()).thenReturn(true);
        assertTrue("is a specflow project, so execute",sensor.shouldExecuteOnProject(project));       
    }
    
    @Test
    public void checkMAtchesIsCalledCorrectly() {
        when(project.getName()).thenReturn("SpecFlow");
        when(project.isModule()).thenReturn(true);
        when(configuration.matches(Tool.OPENCOVER, Mode.ACTIVE)).thenReturn(false); // on purpose
        assertFalse("really checks on tool and mode",sensor.shouldExecuteOnProject(project));       
    }
    

    @Test
    public void analyseSpecFlow() {
        when(project.getName()).thenReturn("SpecFlow"); 
        sensor.analyse(project, context); 
        verify(integrationTestRunner,times(1)).execute(); // runner always executes
        verify(saverSensor,times(0)).analyse(project,context); // should not save coverage
        verify(testResultsSaver,times(1)).save(context, projectUnitTestResults); // save the test results
    }
    
    @Test
    public void analyseNormal() {
        when(project.getName()).thenReturn("Normal"); 
        sensor.analyse(project, context);
        verify(integrationTestRunner,times(1)).execute();// runner always executes
        verify(saverSensor,times(1)).analyse(project,context); // save coverage
        verify(testResultsSaver,times(0)).save(context, projectUnitTestResults); // do not save the test results
    }
    
    
}
