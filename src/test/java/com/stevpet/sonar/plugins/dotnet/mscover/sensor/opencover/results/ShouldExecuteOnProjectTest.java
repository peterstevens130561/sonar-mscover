package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.results;


import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.sensor.AbstractDotNetSensor;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.OpenCoverCoverageResultsSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
public class ShouldExecuteOnProjectTest {

    private AbstractDotNetSensor sensor;
    private VisualStudioProject vsProject;
    private Project project; 
    private Settings settings;
    private VsTestEnvironment vsTestEnvironment;
    @Before
    public void before() {
        project=mock(Project.class);
        MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        vsProject = mock(VisualStudioProject.class);
        when(microsoftWindowsEnvironment.getCurrentProject(anyString())).thenReturn(vsProject);
        settings = mock(Settings.class);
        vsTestEnvironment = new VsTestEnvironment();
        sensor = new OpenCoverCoverageResultsSensor(microsoftWindowsEnvironment,settings, vsTestEnvironment, null);
          
    }
    
    @Test
    public void createSensor_expect() {
       assertNotNull(sensor);
    }
    
    @Test
    public void testCSProject_ShouldNotExecute() {
        when(project.getLanguageKey()).thenReturn("cs");
        when(project.isRoot()).thenReturn(false);
        when(vsProject.isTest()).thenReturn(true);
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularCSProject_ShouldNotExecute() {
        when(project.getLanguageKey()).thenReturn("cs");
        when(project.isRoot()).thenReturn(false);
        when(vsProject.isTest()).thenReturn(false);
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularCSProjectOpenCoverMode_ShouldExecute() {
        when(project.getLanguageKey()).thenReturn("cs");
        when(project.isRoot()).thenReturn(false);
        when(vsProject.isTest()).thenReturn(false);
        when(settings.getString(matches(PropertiesHelper.MSCOVER_COVERAGETOOL))).thenReturn("opencover");
        when(settings.getString(matches(PropertiesHelper.MSCOVER_MODE))).thenReturn("runvstest");
        vsTestEnvironment.setTestsHaveRun();
        assertTrue(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularOtherProjectOpenCoverMode_ShouldNotExecute() {
        when(project.getLanguageKey()).thenReturn("cpp");
        when(project.isRoot()).thenReturn(false);
        when(vsProject.isTest()).thenReturn(false);
        when(settings.getString(matches(PropertiesHelper.MSCOVER_MODE))).thenReturn("runvstest");
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularCSRootProjectOpenCoverMode_ShouldNotExecute() {
        when(project.getLanguageKey()).thenReturn("cs");
        when(project.isRoot()).thenReturn(true);
        when(vsProject.isTest()).thenReturn(false);
        when(settings.getString(matches(PropertiesHelper.MSCOVER_MODE))).thenReturn("runvstest");
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
}