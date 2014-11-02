package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractAssembliesFinder;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;


public class DefaultAssembliesFinderAssembliesTest extends AssembliesFinderTestUtils {
    
    
    @Before
    public void before() {
        propertiesHelper = mock(PropertiesHelper.class);
        finder = new AssembliesFinderFactory().create(propertiesHelper);
        projects = new ArrayList<VisualStudioProject>();
        project = mock(VisualStudioProject.class);
    }
    
    @Test
    public void createAssembliesFinderWithNull_ExpectNullPointerException(){
       finder = new AssembliesFinderFactory().create(null); 
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
        givenProjectIsNotAUnitTestProject();
        projects.add(project);
        List<String> assemblies = fromBuildConfiguration(projects);
        assertEquals(0,assemblies.size());
    }

    @Test
    public void oneProjectIsTestProjectKnownConfig_ExpectInList() {
        //Arrange    
        String unitTestPath = getPathToExistingUnitTestDll();    
        givenProjectIsUnitTestProject("TestDll.txt");
        givenProjectExistsForConfigurationAndPlatform(unitTestPath);
        mockSettingsToUseConfiguration();
        
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
        String name="DoesNotExist.txt";
        String unitTestPath = getPathToANonExistingDll(name);
        
        givenProjectIsUnitTestProject(name);
        givenProjectDoesNotExistForConfigurationAndPlatform();
        
        mockSettingsToUseConfiguration();
        
        projects.add(project);
        //Act
        List<String> assemblies = fromBuildConfiguration(projects);
        
        //Assert
        assertEquals(1,assemblies.size());
        assertEquals(unitTestPath,assemblies.get(0));
    }
}
