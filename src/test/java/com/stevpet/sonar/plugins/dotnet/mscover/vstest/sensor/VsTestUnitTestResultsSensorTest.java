package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.TimeMachineMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.VsTestRunnerMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorMock;

public class VsTestUnitTestResultsSensorTest {
    private TestWrapper classUnderTest = new TestWrapper();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private TimeMachineMock timeMachineMock = new TimeMachineMock();
    private VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private ResourceMediatorFactoryMock resourceMediatorFactoryMock = new ResourceMediatorFactoryMock();
    private ProjectMock projectMock = new ProjectMock();
    private SensorContextMock sensorContextMock = new SensorContextMock();
    private ResourceMediatorMock resourceMediatorMock = new ResourceMediatorMock();
    private VsTestUnitTestResultsAnalyserMock vsTestUnitTestResultsAnalyserMock = new VsTestUnitTestResultsAnalyserMock();
    
    @Before
    public void before() {
        
    }
    
    @Test
    public void analyse_shouldpass() {
       classUnderTest.givenNewSensor(microsoftWindowsEnvironmentMock,msCoverPropertiesMock,timeMachineMock,vsTestEnvironmentMock);
       resourceMediatorFactoryMock.onCreate(resourceMediatorMock);
       classUnderTest.givenResourceMediatorFactory(resourceMediatorFactoryMock);
       classUnderTest.givenVsTestUnitTestResultsAnalyserMock(vsTestUnitTestResultsAnalyserMock);
       classUnderTest.analyse(projectMock,sensorContextMock);
    }
}
