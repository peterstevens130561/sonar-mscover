package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.results;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.OpenCoverCoverageResultsSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;


public class OpenCoverCoverageResultsBaseTest {

    protected FileSystemMock fileSystemMock = new FileSystemMock();
    protected MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    protected MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    protected VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    protected MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    protected OpenCoverCoverageResultsSensor sensor ;
    protected ProjectMock projectMock = new ProjectMock();
    
    public void before() {
        sensor = new OpenCoverCoverageResultsSensor(
                microsoftWindowsEnvironmentMock.getMock(),
                msCoverPropertiesMock.getMock(),
                vsTestEnvironmentMock.getMock(),
                fileSystemMock.getMock(),
                measureSaverMock.getMock()
                );
       
                
                
    }
}
