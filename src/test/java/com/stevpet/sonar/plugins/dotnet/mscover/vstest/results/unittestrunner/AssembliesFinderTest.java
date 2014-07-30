package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.unittestrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.NoAssemblyDefinedMsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.SolutionHasNoProjectsSonarException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;



public class AssembliesFinderTest {
    
    
    private PropertiesHelper propertiesHelper;
    private Settings settings ;
    
    @Before
    public void before() {
        settings = mock(Settings.class);
        propertiesHelper = propertiesHelper.create(settings);
    }
    
    @Test(expected=NullPointerException.class)
    public void createAssembliesFinderWithNull_ExpectNullPointerException(){
        AssembliesFinder finder = AssembliesFinder.create(null);        
    }
    
    @Test (expected = SolutionHasNoProjectsSonarException.class)
    public void noProjectsList_ExpectSonarException() {
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        finder.findUnitTestAssemblies(null);
    }
    
    @Test(expected = SolutionHasNoProjectsSonarException.class)
    public void noProjects_ExpectSonarException() {
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        finder.findUnitTestAssemblies(null);
    
    }
    
    @Test
    public void oneProjectNotTestProject_ExpectEmptyList() {
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        VisualStudioProject project = mock(VisualStudioProject.class);
        when(project.isUnitTest()).thenReturn(false);
        projects.add(project);
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        List<String> assemblies = finder.findUnitTestAssemblies(projects);
        assertEquals(0,assemblies.size());
    }
    
    @Test
    public void oneProjectIsTestProjectKnownConfig_ExpectInList() {
        //Arrange
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        VisualStudioProject project = mock(VisualStudioProject.class);
        
        String buildConfiguration="Debug";
        String buildPlatform="Any CPU";
        String unitTestPath="C:\\a\\b\\c\\unittest.dll";
        
        when(project.isUnitTest()).thenReturn(true);
        when(project.getArtifact(eq(buildConfiguration), eq(buildPlatform))).thenReturn(new File(unitTestPath));
        
        propertiesHelper = mock(PropertiesHelper.class);
        when(propertiesHelper.getRequiredBuildConfiguration()).thenReturn(buildConfiguration);
        when(propertiesHelper.getRequiredBuildPlatform()).thenReturn(buildPlatform);
        
        projects.add(project);
        //Act
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        List<String> assemblies = finder.findUnitTestAssemblies(projects);
        
        //Assert
        assertEquals(1,assemblies.size());
        assertEquals(unitTestPath,assemblies.get(0));
    }
    
    @Test(expected=NoAssemblyDefinedMsCoverException.class)
    public void oneProjectIsTestProjectUnknownConfig_ExpectException() {
        //Arrange
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        VisualStudioProject project = mock(VisualStudioProject.class);
        
        String buildConfiguration="Debug";
        String buildPlatform="Any CPU";
        String unitTestPath="C:/a/b/c/unittest.dll";
        
        when(project.isUnitTest()).thenReturn(true);
        when(project.getArtifact(eq(buildConfiguration), eq(buildPlatform))).thenReturn(null);
        
        propertiesHelper = mock(PropertiesHelper.class);
        when(propertiesHelper.getRequiredBuildConfiguration()).thenReturn(buildConfiguration);
        when(propertiesHelper.getRequiredBuildPlatform()).thenReturn(buildPlatform);
        
        projects.add(project);
        //Act
        AssembliesFinder finder = AssembliesFinder.create(propertiesHelper);
        List<String> assemblies = finder.findUnitTestAssemblies(projects);
        
        //Assert
        assertEquals(1,assemblies.size());
        assertEquals(unitTestPath,assemblies.get(0));
    }
}
