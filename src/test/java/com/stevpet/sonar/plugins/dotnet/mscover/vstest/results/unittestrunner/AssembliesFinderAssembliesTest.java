package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.unittestrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssemblyDefinedMsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.SolutionHasNoProjectsSonarException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.eq;



public class AssembliesFinderAssembliesTest {
    
    
    private PropertiesHelper propertiesHelper;
    private AssembliesFinder finder ;
    
    @Before
    public void before() {
        propertiesHelper = mock(PropertiesHelper.class);
        finder = AssembliesFinder.create(propertiesHelper);
    }
    
    @Test
    public void createAssembliesFinderWithNull_ExpectNullPointerException(){
       finder = AssembliesFinder.create(null); 
       assertNotNull(finder);
    }
    
    @Test (expected = SolutionHasNoProjectsSonarException.class)
    public void noProjectsList_ExpectSonarException() {
        fromBuildConfiguration(null);
    }
    
    @Test(expected = SolutionHasNoProjectsSonarException.class)
    public void noProjects_ExpectSonarException() {
        fromBuildConfiguration(null);
    
    }
    
    @Test
    public void oneProjectNotTestProject_ExpectEmptyList() {
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        VisualStudioProject project = mock(VisualStudioProject.class);
        when(project.isUnitTest()).thenReturn(false);
        projects.add(project);
        List<String> assemblies = fromBuildConfiguration(projects);
        assertEquals(0,assemblies.size());
    }
    
    @Test
    public void oneProjectIsTestProjectKnownConfig_ExpectInList() {
        //Arrange
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        VisualStudioProject project = mock(VisualStudioProject.class);
        
        String buildConfiguration="Debug";
        String buildPlatform="AnyCPU";
        String unitTestPath="C:\\a\\b\\c\\unittest.dll";
        
        when(project.isUnitTest()).thenReturn(true);
        when(project.getArtifact(eq(buildConfiguration), eq(buildPlatform))).thenReturn(new File(unitTestPath));
        
        when(propertiesHelper.getRequiredBuildConfiguration()).thenReturn(buildConfiguration);
        when(propertiesHelper.getRequiredBuildPlatform()).thenReturn(buildPlatform);
        
        projects.add(project);
        //Act
        List<String> assemblies = fromBuildConfiguration(projects);
        
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
        
        when(propertiesHelper.getRequiredBuildConfiguration()).thenReturn(buildConfiguration);
        when(propertiesHelper.getRequiredBuildPlatform()).thenReturn(buildPlatform);
        
        projects.add(project);
        //Act

        List<String> assemblies = fromBuildConfiguration(projects);
        
        //Assert
        assertEquals(1,assemblies.size());
        assertEquals(unitTestPath,assemblies.get(0));
    }
    
    private List<String> fromBuildConfiguration(
            List<VisualStudioProject> projects) {
        return finder.findUnitTestAssembliesFromConfig(null, projects);
    }
}
