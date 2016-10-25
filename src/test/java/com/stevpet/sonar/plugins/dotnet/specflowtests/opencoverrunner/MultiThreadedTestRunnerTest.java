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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.mockito.Mock;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;

import org.sonar.api.batch.fs.FileSystem;


import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.housekeeping.OrphanedTestRunnerRemover;
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
    @Mock private MultiThreadedSpecflowIntegrationTestCache multiThreadedSpecflowIntegrationTestCache;
    @Mock private OrphanedTestRunnerRemover orphanedTestRunnerRemover;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);

        multiThreadedSpecflowIntegrationTestRunner = new MultiThreadedSpecflowIntegrationTestApplication(
                microsoftWindowsEnvironment,
                integrationTestsConfiguration,
                testRunnerFactory,
                fileSystem,
                multiThreadedSpecflowIntegrationTestCache,
                orphanedTestRunnerRemover);
        //Fluent stuff
        when(testRunner.setCoverageFile(any(File.class))).thenReturn(testRunner);
        when(testRunner.setProjectName(any(String.class))).thenReturn(testRunner);
        when(testRunner.setModule(any(String.class))).thenReturn(testRunner);
        when(testRunner.setTimeout(120)).thenReturn(testRunner);
        when(testRunner.setCoverageRoot(any(File.class))).thenReturn(testRunner);
        when(integrationTestsConfiguration.getTestRunnerThreads()).thenReturn(5);
        when(integrationTestsConfiguration.getTestRunnerTimeout()).thenReturn(5);
    }
    
   // @Test
    public void noProjects() {
        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(projects);
        multiThreadedSpecflowIntegrationTestRunner.execute();
        verify(testRunnerFactory,times(0)).create();
    }
    
   // @Test
    public void oneProject() {
        VisualStudioProject project = mock(VisualStudioProject.class);
        when(project.getAssemblyName()).thenReturn("myproject");
        when(testRunnerFactory.create()).thenReturn(testRunner);
        projects.add(project);
        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(projects);
        multiThreadedSpecflowIntegrationTestRunner.execute();

        verify(testRunnerFactory,times(1)).create();
        verify(testRunner,times(1)).execute();
        verify(orphanedTestRunnerRemover,times(1)).execute();
    }
    
   // @Test
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
        verify(orphanedTestRunnerRemover,times(1)).execute();
    }
    
    //@Test
    /*
     * Tests that runner throws an exception if one of the treads fail
     */
    public void crashitProjects() {
        VisualStudioProject project = mock(VisualStudioProject.class);
        when(project.getAssemblyName()).thenReturn("myproject");
        when(testRunnerFactory.create()).thenReturn(testRunner);
        projects.add(project);
        when(microsoftWindowsEnvironment.getTestProjects(any(Pattern.class))).thenReturn(projects);
        doThrow(new IllegalStateException()).when(testRunner).execute();
        try {
            multiThreadedSpecflowIntegrationTestRunner.execute();
        } catch (IllegalStateException e) {
            return; // ok
        }

        //Should not get here
        fail("should have gotten an exception");

    }
}
