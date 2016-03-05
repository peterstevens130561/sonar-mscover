package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;

public class MultiThreadedTestRunnerTest {
    @Mock private MultiThreadedSpecflowIntegrationTestApplication multiThreadedSpecflowIntegrationTestRunner ;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private IntegrationTestsConfiguration integrationTestsConfiguration;
    @Mock private IntegrationTestRunnerFactory testRunnerFactory;
    @Mock private FileSystem fileSystem;
    @Mock private IntegrationTestRunner testRunner ;
    private List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        multiThreadedSpecflowIntegrationTestRunner=new MultiThreadedSpecflowIntegrationTestApplication(microsoftWindowsEnvironment, integrationTestsConfiguration, testRunnerFactory, fileSystem);  
        //Fluent stuff
        when(testRunner.setCoverageFile(any(File.class))).thenReturn(testRunner);
        when(testRunner.setProjectName(any(String.class))).thenReturn(testRunner);
        when(testRunner.setModule(any(String.class))).thenReturn(testRunner);
        when(testRunner.setCoverageRoot(any(File.class))).thenReturn(testRunner);
        when(integrationTestsConfiguration.getTestRunnerThreads()).thenReturn(5);
        when(integrationTestsConfiguration.getTestRunnerTimeout()).thenReturn(5);
    }
    
    @Test
    public void noProjects() {
        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(projects);
        multiThreadedSpecflowIntegrationTestRunner.execute();
        verify(testRunnerFactory,times(0)).create();
    }
    
    @Test
    public void oneProject() {
        VisualStudioProject project = mock(VisualStudioProject.class);
        when(project.getAssemblyName()).thenReturn("myproject");
        when(testRunnerFactory.create()).thenReturn(testRunner);
        projects.add(project);
        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(projects);
        multiThreadedSpecflowIntegrationTestRunner.execute();

        verify(testRunnerFactory,times(1)).create();
        verify(testRunner,times(1)).execute();
    }
    
    @Test
    public void twoProjects() {
        when(integrationTestsConfiguration.getCoverageReaderThreads()).thenReturn(5);
        VisualStudioProject projectA = mock(VisualStudioProject.class);
        when(projectA.getAssemblyName()).thenReturn("myproject1");
        when(testRunnerFactory.create()).thenReturn(testRunner);
        projects.add(projectA);

        VisualStudioProject projectB = mock(VisualStudioProject.class);
        when(projectB.getAssemblyName()).thenReturn("myproject2");
        when(testRunnerFactory.create()).thenReturn(testRunner);
        projects.add(projectB);

        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(projects);
        multiThreadedSpecflowIntegrationTestRunner.execute();

        verify(testRunnerFactory,times(2)).create();
        verify(testRunner,times(2)).execute();
    }
    
    @Test
    /*
     * Tests that runner throws an exception if one of the treads fail
     */
    public void crashitProjects() {
        VisualStudioProject project = mock(VisualStudioProject.class);
        when(project.getAssemblyName()).thenReturn("myproject");
        when(testRunnerFactory.create()).thenReturn(testRunner);
        projects.add(project);
        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(projects);
        doThrow(new SonarException()).when(testRunner).execute();
        try {
            multiThreadedSpecflowIntegrationTestRunner.execute();
        } catch (SonarException e) {
            return; // ok
        }

        //Should not get here
        fail("should have gotten an exception");

    }
}
