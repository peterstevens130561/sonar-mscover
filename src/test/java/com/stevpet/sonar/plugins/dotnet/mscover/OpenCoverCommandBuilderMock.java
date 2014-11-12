package com.stevpet.sonar.plugins.dotnet.mscover;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCommandMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.VsTestRunnerMock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.any;

public class OpenCoverCommandBuilderMock extends GenericClassMock<OpenCoverCommandBuilder> {
    public OpenCoverCommandBuilderMock() {
        super(OpenCoverCommandBuilder.class);
    }

    public void verifySetOpenCovercommand(
            OpenCoverCommandMock openCoverCommandMock) {
        // TODO Auto-generated method stub
        verify(instance,times(1)).setOpenCoverCommand(openCoverCommandMock.getMock());
    }

    public void verifySetSolution() {
        verify(instance,times(1)).setSolution(any(VisualStudioSolution.class));       
    }
    
    public void verifySetMsCoverProperties(MsCoverPropertiesMock msCoverPropertiesMock) {
        verify(instance,times(1)).setMsCoverProperties(msCoverPropertiesMock.getMock());
    }
    
    public void verifySetTestRunner(VsTestRunnerMock vsTestRunnerMock) {
        verify(instance,times(1)).setTestRunner(vsTestRunnerMock.getMock());
    }
}
