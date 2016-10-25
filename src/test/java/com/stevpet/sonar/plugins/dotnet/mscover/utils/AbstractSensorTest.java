/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
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
