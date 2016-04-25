package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.Sensor;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

import static org.mockito.Mockito.when;

public class OpenCoverSpecFlowTestSensorTest {

    private Sensor sensor ;
    @Mock private Project project;
    @Mock private IntegrationTestsConfiguration configuration;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        sensor = new OpenCoverSpecFlowTestSensor(configuration);
        Pattern specflowPattern = Pattern.compile(".*SpecFlow.*");
        when(configuration.getTestProjectPattern()).thenReturn(specflowPattern);
        when(project.isModule()).thenReturn(true);
    }
    
    @Test
    public void shouldSkipNormalProject() {
        when(project.getName()).thenReturn("someproject");
        when(project.isModule()).thenReturn(true);
        assertFalse("is a normal project, so do not execute",sensor.shouldExecuteOnProject(project));      
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
}
