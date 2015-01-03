package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.results;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.AbstractDotNetSensor;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCoverageResultsSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

public class AnalyseTest {
    
    private AbstractDotNetSensor sensor;
    private VisualStudioProject vsProject;
    private Project project; 
    private MsCoverProperties propertiesHelper;
    private SensorContext context;
    @Before
    public void before() {
        project=mock(Project.class);
        MicrosoftWindowsEnvironment microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        vsProject = mock(VisualStudioProject.class);
        when(microsoftWindowsEnvironment.getCurrentProject(anyString())).thenReturn(vsProject);
        propertiesHelper = mock(PropertiesHelper.class);
        VsTestEnvironment vsTestEnvironment = new VsTestEnvironment();
        sensor = new OpenCoverCoverageResultsSensor(microsoftWindowsEnvironment,propertiesHelper, vsTestEnvironment, null);
          
    }
    
    @Test
    public void dummyTest() {
        //sensor.analyse(project, context);
        assertTrue(true);
    }
    
    @Test
    public void fullBlownTest() {
        // load the test data
        // pass a project
        // check number of times file measures are updated
        // check number of times line measures are updated
        // check for one file whether the number of branches is correct
        // check for one file whethter the numbe of lines is correct/
    }
}
