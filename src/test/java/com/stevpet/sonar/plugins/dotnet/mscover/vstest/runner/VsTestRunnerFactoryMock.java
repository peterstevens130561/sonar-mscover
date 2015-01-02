package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import org.sonar.api.scan.filesystem.ModuleFileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.VsTestRunnerMock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

public class VsTestRunnerFactoryMock extends GenericClassMock<AbstractVsTestRunnerFactory> {
    
    public VsTestRunnerFactoryMock() {
        super(AbstractVsTestRunnerFactory.class);
    }

    public void onCreate(VsTestRunnerMock vsTestRunnerMock) {
        // TODO Auto-generated method stub
        when(instance.createBasicTestRunnner(any(MsCoverProperties.class), 
                any(ModuleFileSystem.class),
                any(MicrosoftWindowsEnvironment.class)))
                .thenReturn(vsTestRunnerMock.getMock());
    }
}
