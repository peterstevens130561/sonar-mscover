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
    @Mock private OpenCoverSpecFlowTestRunnerSensor runnerSensor;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        sensor = new OpenCoverSpecFlowTestSensor(configuration,runnerSensor, saverSensor);
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
        verify(integrationTestRunner,times(1)).execute();
        verify(saverSensor,times(0)).analyse(project,context);
    }
    
    @Test
    public void analyseNormal() {
        when(project.getName()).thenReturn("Normal"); 
        sensor.analyse(project, context);
        verify(integrationTestRunner,times(1)).execute();
        verify(saverSensor,times(1)).analyse(project,context);
    }
    
    
}
