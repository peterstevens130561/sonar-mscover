package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;

import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.AssembliesFinderMock;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class AssembliesFinderFactoryMock extends GenericClassMock<AssembliesFinderFactory> {
    public AssembliesFinderFactoryMock() {
        super(AssembliesFinderFactory.class);
    }

    public void onCreate(MsCoverPropertiesMock msCoverPropertiesMock,AssembliesFinderMock assembliesFinderMock) {
        when(instance.create(msCoverPropertiesMock.getMock())).thenReturn(assembliesFinderMock.getMock());
    }

}
