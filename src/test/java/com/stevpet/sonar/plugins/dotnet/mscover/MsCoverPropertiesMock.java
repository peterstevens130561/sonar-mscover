package com.stevpet.sonar.plugins.dotnet.mscover;

import java.util.Collection;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.when;

public class MsCoverPropertiesMock {
    private MsCoverProperties properties = mock(MsCoverProperties.class);
    
    public MsCoverProperties getMock() {
        return properties;
    }

    public void givenRunOpenCover(boolean b) {
        when(properties.runOpenCover()).thenReturn(b);
    }

    public void givenUnitTestHintPath(String hintPath) {
        when(properties.getUnitTestHintPath()).thenReturn(hintPath);
    }
    
    public void givenUnitTestAssembliesThatCanBeIgnoredIfMissing(Collection<String> list) {
        when(properties.getUnitTestAssembliesThatCanBeIgnoredIfMissing()).thenReturn(list);
    }

    public void givenRequiredBuildPlatform(String buildPlatform) {
        when(properties.getRequiredBuildPlatform()).thenReturn(buildPlatform);
    }

    public void givenRequiredBuildConfiguration(String buildConfiguration) {
        when(properties.getRequiredBuildConfiguration()).thenReturn(buildConfiguration);
    }
    
    public void givenOpenCoverSkipAutoProps(boolean value) {
        when(properties.getOpenCoverSkipAutoProps()).thenReturn(value);
    }

    /**
     * 
     * @param cutOffDate - the string to return when getCutOffDate is used.
     */
    public void givenCutOffDate(String cutOffDate) {
        when(properties.getCutOffDate()).thenReturn(cutOffDate); 
    }

    /**
     * @param path - the path to return when getIntegrationTestsPath is used.
     */
    public void givenIntegrationTestsPath(String path) {
        when(properties.getIntegrationTestsPath()).thenReturn(path);
    }

    /**
     * @param enabled - to return when isIntegrationTestsEnabled is used
     */
    public void givenIntegrationTestsEnabled(boolean enabled) {
        when(properties.isIntegrationTestsEnabled()).thenReturn(enabled);
    }

    /**
     * @param enabled - to return when isUnitTestsTestsEnabled is used
     */
    public void givenUnitTestsEnabled(boolean enabled) {
        when(properties.isUnitTestsEnabled()).thenReturn(enabled);      
    }

    /**
     * 
     * @param mode - to return when getMode is used
     */
    public void givenMode(String mode) {
        when(properties.getMode()).thenReturn(mode);
    }
 }
