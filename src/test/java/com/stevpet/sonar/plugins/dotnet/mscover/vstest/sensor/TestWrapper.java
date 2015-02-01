package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;

public class TestWrapper {

    private VsTestUnitTestResultsSensor sensor;
    public void givenNewSensor(
            MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock,
            MsCoverPropertiesMock msCoverPropertiesMock,
            TimeMachineMock timeMachineMock,
            VsTestEnvironmentMock vsTestEnvironmentMock) {
        
        sensor=new VsTestUnitTestResultsSensor(
                msCoverPropertiesMock.getMock(),
                vsTestEnvironmentMock.getMock(),null,
                microsoftWindowsEnvironmentMock.getMock(),null);
                
    }
    
    public void analyse(ProjectMock projectMock,
            SensorContextMock sensorContextMock) {
        sensor.analyse(projectMock.getMock(), sensorContextMock.getMock());
    }


    public void givenVsTestUnitTestResultsAnalyserMock(
            VsTestUnitTestResultsAnalyserMock vsTestUnitTestResultsAnalyserMock) {
        sensor.setVsTestUnitTestResultsAnalyser(vsTestUnitTestResultsAnalyserMock.getMock());
        
    }

}
