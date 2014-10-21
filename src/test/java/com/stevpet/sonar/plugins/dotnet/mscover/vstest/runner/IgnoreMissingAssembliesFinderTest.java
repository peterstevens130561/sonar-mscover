package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.exceptions.MsCoverUnitTestAssemblyDoesNotExistException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AbstractAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.IgnoreMissingAssembliesFinder;

public class IgnoreMissingAssembliesFinderTest extends AssembliesFinderTestUtils {
    
    @Before
    public void before() {
        propertiesHelper = mock(PropertiesHelper.class);
        finder = new AssembliesFinderFactory().create(propertiesHelper);
        projects = new ArrayList<VisualStudioProject>();
        project = mock(VisualStudioProject.class);
    }
    
    @Test
    public void oneProjectIsTestProjectDoesNotExistInIgnored_ExpectNotInList() {
        //Arrange    
        String name="DoesNotExist.txt";
        String unitTestPath = getPathToANonExistingDll(name);
        addNonExistingDllToIgnoreMissingList(name);
        finder = new AssembliesFinderFactory().create(propertiesHelper);
        Logger myLog = mock(Logger.class);
        IgnoreMissingAssembliesFinder myFinder = (IgnoreMissingAssembliesFinder) finder;
        myFinder.setLogger(myLog);
        projectIsUnitTestProject(name);
        projectExistsForConfigurationAndPlatform(unitTestPath);
        mockSettingsToUseConfiguration();
        
        projects.add(project);
        //Act
        List<String> assemblies = fromBuildConfiguration(projects);
        
        //Assert
        assertEquals(0,assemblies.size());
        verify(myLog,times(1)).warn(anyString(),anyString());
        verify(myLog,times(0)).error(anyString(),anyString());
     
    }
    
    @Test
    public void oneProjectIsTestProjectDoesNotExistNotInIgnored_ExpectException() {
        //Arrange    
        String name="DoesNotExist.txt";

        String unitTestPath = getPathToANonExistingDll(name);
        addNonExistingDllToIgnoreMissingList("SomeOtherName.dll");
        finder = new AssembliesFinderFactory().create(propertiesHelper);
        IgnoreMissingAssembliesFinder myFinder = (IgnoreMissingAssembliesFinder) finder;
        Logger myLog = mock(Logger.class);
        myFinder.setLogger(myLog);
        projectIsUnitTestProject(name);
        projectExistsForConfigurationAndPlatform(unitTestPath);
        mockSettingsToUseConfiguration();
        
        projects.add(project);
        boolean caught=false;
        //Act
        try {
            fromBuildConfiguration(projects);
        } catch (MsCoverUnitTestAssemblyDoesNotExistException e) {
            caught=true;
        }
        assertFalse("MsCoveUnitTestAssemblyDoesNotExistException not thrown",caught);
        verify(myLog,times(0)).warn(anyString(),anyString());
        verify(myLog,times(1)).error(anyString(),anyString());
        //Assert
     
    }

    private void assertFalse(String string, boolean caught) {
        // TODO Auto-generated method stub
        
    }
}
