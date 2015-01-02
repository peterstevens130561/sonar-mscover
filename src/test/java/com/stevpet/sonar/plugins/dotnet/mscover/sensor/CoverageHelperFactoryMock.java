package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

import static org.mockito.Mockito.*;


public class CoverageHelperFactoryMock extends GenericClassMock<AbstractCoverageHelperFactory> {

    public CoverageHelperFactoryMock() {
        super(AbstractCoverageHelperFactory.class);
    }

    public void WhenCreateShouldExecuteHelper(ShouldExecuteHelperMock mock ) {
        when(instance.createShouldExecuteHelper(any(MsCoverProperties.class))).thenReturn(mock.getMock());
    }

    public void whencreateIntegrationTestCoverageHelper(
            CoverageHelperMock coverageHelperMock) {
        when(instance.createIntegrationTestCoverageHelper(any(MsCoverProperties.class), any(MicrosoftWindowsEnvironment.class), any(MeasureSaver.class))).thenReturn(coverageHelperMock.getMock());
    }
}
