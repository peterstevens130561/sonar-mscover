package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.sonar.api.batch.SensorContext;
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
        integrationTestSensorHelper = new IntegrationTestSensorHelper(microsoftWindowsEnvironment);
    }
    
    @Test
    public void noPattern_False() {
        when(integrationTestsConfiguration.getTestProjectPattern()).thenReturn(null);
        assertFalse("no pattern, should not execute",integrationTestSensorHelper.isSolutionWithIntegrationTestProjects(project,null));     
    }
    
    @Test
    public void pattern_root_False() {  
        when(project.isModule()).thenReturn(false);
        assertFalse("pattern defined but root should not execute",integrationTestSensorHelper.isSolutionWithIntegrationTestProjects(project,pattern));
    }
    
    @Test
    public void pattern_child_trigger() { 
        when(project.isModule()).thenReturn(true);
        when(microsoftWindowsEnvironment.hasTestProjects(any(Pattern.class))).thenReturn(true);
        assertTrue("pattern and module, testprojects",integrationTestSensorHelper.isSolutionWithIntegrationTestProjects(project,pattern));
    }
    
    @Test
    public void pattern_child_noTestProjects() {
        when(project.isModule()).thenReturn(true);
        when(microsoftWindowsEnvironment.hasTestProjects(any(Pattern.class))).thenReturn(false);
        assertFalse("pattern and module, no testprojects",integrationTestSensorHelper.isSolutionWithIntegrationTestProjects(project,pattern));
    }
    
    
 
}
