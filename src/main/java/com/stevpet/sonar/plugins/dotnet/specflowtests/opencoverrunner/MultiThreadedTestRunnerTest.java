package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class MultiThreadedTestRunnerTest {
    @Mock private MultiThreadedSpecflowIntegrationTestRunner multiThreadedSpecflowIntegrationTestRunner ;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private IntegrationTestsConfiguration integrationTestsConfiguration;
    @Mock private IntegrationTestRunnerFactory testRunnerFactory;
    @Mock private FileSystem fileSystem;
    private List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        multiThreadedSpecflowIntegrationTestRunner=new MultiThreadedSpecflowIntegrationTestRunner(microsoftWindowsEnvironment, integrationTestsConfiguration, testRunnerFactory, fileSystem);       
    }
    
    @Test
    public void noProjects() {
        when(integrationTestsConfiguration.getCoverageReaderThreads()).thenReturn(5);
        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(projects);
        multiThreadedSpecflowIntegrationTestRunner.execute();
        verify(testRunnerFactory,times(0)).create();
    }
    
    @Test
    public void oneProject() {
        when(integrationTestsConfiguration.getCoverageReaderThreads()).thenReturn(5);
        VisualStudioProject project = mock(VisualStudioProject.class);
        when(project.getAssemblyName()).thenReturn("myproject");
        projects.add(project);
        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(new ArrayList<VisualStudioProject>());
        multiThreadedSpecflowIntegrationTestRunner.execute();

        verify(testRunnerFactory,times(1)).create();
    }
}
