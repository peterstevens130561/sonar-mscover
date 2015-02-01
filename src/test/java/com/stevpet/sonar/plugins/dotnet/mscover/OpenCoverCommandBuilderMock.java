package com.stevpet.sonar.plugins.dotnet.mscover;


import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCommandMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.VsTestRunnerMock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyListOf;

public class OpenCoverCommandBuilderMock extends GenericClassMock<OpenCoverCommandBuilder> {
    public OpenCoverCommandBuilderMock() {
        super(OpenCoverCommandBuilder.class);
    }

    public void verifySetOpenCovercommand(
            OpenCoverCommandMock openCoverCommandMock) {
        verify(instance,times(1)).setOpenCoverCommand(openCoverCommandMock.getMock());
    }

    public void verifySetAssemblies() {
        verify(instance,times(1)).setAssemblies(anyListOf(String.class)); 
    }
    
    public void verifySetMsCoverProperties(MsCoverPropertiesMock msCoverPropertiesMock) {
        verify(instance,times(1)).setMsCoverProperties(msCoverPropertiesMock.getMock());
    }
    
    public void verifySetTestRunner(VsTestRunnerMock vsTestRunnerMock) {
        verify(instance,times(1)).setTestRunner(vsTestRunnerMock.getMock());
    }
}
