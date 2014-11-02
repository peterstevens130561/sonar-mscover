package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

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
 }
