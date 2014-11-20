package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.AssembliesFinderMock;

public class AssembliesFinderFactoryMock extends GenericClassMock<AssembliesFinderFactory> {
    public AssembliesFinderFactoryMock() {
        super(AssembliesFinderFactory.class);
    }

    public void onCreate(MsCoverPropertiesMock msCoverPropertiesMock,AssembliesFinderMock assembliesFinderMock) {
        when(instance.create(msCoverPropertiesMock.getMock())).thenReturn(assembliesFinderMock.getMock());
    }

}
