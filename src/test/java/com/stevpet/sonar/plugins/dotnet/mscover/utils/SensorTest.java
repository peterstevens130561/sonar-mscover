package com.stevpet.sonar.plugins.dotnet.mscover.utils;

import static org.picocontainer.Characteristics.CACHE;

import org.picocontainer.DefaultPicoContainer;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

public class SensorTest {

    protected MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    protected MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    protected FileSystemMock fileSystemMock = new FileSystemMock();
    protected DefaultPicoContainer container = new DefaultPicoContainer();
    
    /**
     * returns a new container with mocks for the usual dependencies for sensors 
     * @return
     */
    public DefaultPicoContainer getContainerWithSensorMocks() {
        container.addComponent(msCoverPropertiesMock.getMock());
        container.as(CACHE).addComponent(VsTestEnvironment.class);
        container.addComponent(OpenCoverCommand.class)
        .addComponent(microsoftWindowsEnvironmentMock.getMock())
        .addComponent(fileSystemMock.getMock());
        return container;
    }
}
