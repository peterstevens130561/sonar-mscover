package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.TimeMachine;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverTestResultsSaverSensorTest {

    MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    TimeMachineMock timeMachineMock = new TimeMachineMock();
    
    MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
    MsCoverProperties msCoverProperties;
    private VsTestEnvironment vsTestEnvironment;
    private TimeMachine timeMachine;
    
    @Before
    public void before() {
        microsoftWindowsEnvironment = microsoftWindowsEnvironmentMock.getMock();
        msCoverProperties = msCoverPropertiesMock.getMock();
        vsTestEnvironment = vsTestEnvironmentMock.getMock();
        timeMachine = timeMachineMock.getMock();
    }
    
    @Test
    public void getSupportedLanguages_ExpectCsOnly() {
        OpenCoverTestResultsSaverSensor sensor = new OpenCoverTestResultsSaverSensor(microsoftWindowsEnvironment, 
                msCoverProperties,vsTestEnvironment,timeMachine);
        String[] languages = sensor.getSupportedLanguages();
        
    }
}
