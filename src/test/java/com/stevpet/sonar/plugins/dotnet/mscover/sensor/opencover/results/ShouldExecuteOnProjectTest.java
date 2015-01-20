package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.results;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.AbstractDotNetSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCoverageResultsSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
public class ShouldExecuteOnProjectTest {

    private AbstractDotNetSensor sensor;
    private VisualStudioProject vsProject;
    private Project project; 
    private MsCoverProperties propertiesHelper;
    private VsTestEnvironment vsTestEnvironment;
    private FileSystemMock fileSystemMock = new FileSystemMock();
    @Before
    public void before() {
        project=mock(Project.class);
        MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        vsProject = mock(VisualStudioProject.class);
        when(microsoftWindowsEnvironment.getCurrentProject(anyString())).thenReturn(vsProject);
        propertiesHelper = mock(PropertiesHelper.class);
        vsTestEnvironment = new VsTestEnvironment();
        sensor = new OpenCoverCoverageResultsSensor(microsoftWindowsEnvironment,propertiesHelper, vsTestEnvironment, null,null);
          
    }
    
    @Test
    public void createSensor_expect() {
       assertNotNull(sensor);
    }
    
    @Test
    public void testCSProject_ShouldNotExecute() {
        givenProjectLanguageIsCSharp();
        when(project.isRoot()).thenReturn(false);
        when(vsProject.isTest()).thenReturn(true);
        assertFalse(sensor.shouldExecuteOnProject(project));
    }

    private void givenProjectLanguageIsCSharp() {
        fileSystemMock.givenLanguage("cs");
    }
    
    @Test
    public void regularCSProject_ShouldNotExecute() {
        givenProjectLanguageIsCSharp();
        when(project.isRoot()).thenReturn(false);
        when(vsProject.isTest()).thenReturn(false);
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularCSProjectOpenCoverMode_ShouldExecute() {
        givenProjectLanguageIsCSharp();
        when(project.isRoot()).thenReturn(false);
        when(vsProject.isTest()).thenReturn(false);
        when(propertiesHelper.runOpenCover()).thenReturn(true);
        when(propertiesHelper.getMode()).thenReturn("runvstest");
        //when(settings.getString(matches(PropertiesHelper.MSCOVER_COVERAGETOOL))).thenReturn("opencover");
        //when(settings.getString(matches(PropertiesHelper.MSCOVER_MODE))).thenReturn("runvstest");
        vsTestEnvironment.setTestsHaveRun();
        assertTrue(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularOtherProjectOpenCoverMode_ShouldNotExecute() {
        when(project.getLanguageKey()).thenReturn("cpp");
        when(project.isRoot()).thenReturn(false);
        when(vsProject.isTest()).thenReturn(false);
        when(propertiesHelper.getMode()).thenReturn("runvstest");
        //when(settings.getString(matches(PropertiesHelper.MSCOVER_MODE))).thenReturn("runvstest");
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
    
    @Test
    public void regularCSRootProjectOpenCoverMode_ShouldNotExecute() {
        givenProjectLanguageIsCSharp();
        when(project.isRoot()).thenReturn(true);
        when(vsProject.isTest()).thenReturn(false);
        when(propertiesHelper.getMode()).thenReturn("runvstest");
        //when(settings.getString(matches(PropertiesHelper.MSCOVER_MODE))).thenReturn("runvstest");
        assertFalse(sensor.shouldExecuteOnProject(project));
    }
}
