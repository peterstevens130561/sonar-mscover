package com.stevpet.sonar.plugins.dotnet.mscover.utils;

import static org.picocontainer.Characteristics.CACHE;

import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

public abstract class AbstractSensorTest {

    protected MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    protected MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    protected FileSystemMock fileSystemMock = new FileSystemMock();
    protected DefaultPicoContainer container = new DefaultPicoContainer();
    protected SensorContextMock sensorContextMock = new SensorContextMock();
    /**
     * returns a new container with mocks for the usual dependencies for sensors 
     * @return
     */
    public DefaultPicoContainer getContainerWithSensorMocks() {
        container.addComponent(msCoverPropertiesMock.getMock());
        container.as(CACHE).addComponent(VsTestEnvironment.class);
        container.addComponent(microsoftWindowsEnvironmentMock.getMock())
        .addComponent(fileSystemMock.getMock())
        .addComponent(sensorContextMock.getMock())
        .addComponent(DefaultProcessLock.class);
        return container;
    }
}
