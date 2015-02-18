/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.sensor;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.UnitTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestsCoverSensorTest {

    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    ResourceMediatorMock resourceMediatorMock = new ResourceMediatorMock();
    Sensor sensor;
    ProjectMock projectMock = new ProjectMock();
    SensorContext context ;
    MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
    private FileSystemMock fileSystemMock = new FileSystemMock();
    private CoverageHelperFactoryMock coverageHelperFactoryMock = new CoverageHelperFactoryMock();
    private ShouldExecuteHelperMock shouldExecuteHelperMock = new ShouldExecuteHelperMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    
    @Before
    public void before() {
        context = mock(SensorContext.class);
        coverageHelperFactoryMock.WhenCreateShouldExecuteHelper(shouldExecuteHelperMock);
        microsoftWindowsEnvironment = mock(MicrosoftWindowsEnvironment.class);
        sensor = new IntegrationTestsCoverSensorStub(msCoverPropertiesMock.getMock(),microsoftWindowsEnvironmentMock.getMock());
        msCoverPropertiesMock.givenMode("reuse");

        
    }
    
    
    @Test
    public void IntegrationTestsSensor_PathNotSet_NotEnabled() {
        //Arrange
        projectMock.givenIsRoot(false);

        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(projectMock.getMock());
        //Assert
        assertFalse(shouldExecute);         
    }
    
    @Test
    public void IntegrationTestsSensor_PathSet_Enabled() {
        //Arrange
        msCoverPropertiesMock.givenIntegrationTestsEnabled(true);
        shouldExecuteHelperMock.whenShouldExecute(projectMock.getMock(),true);
        projectMock.givenIsRoot(false);
        //Act
        boolean shouldExecute=sensor.shouldExecuteOnProject(projectMock.getMock());
        //Assert
        assertTrue(shouldExecute); 
    }
     
    
    @Test
    public void saveMeasure_Calls_ShouldBe5() {
        MeasureSaver measureSaver = mock(SonarMeasureSaver.class);
        LineMeasureSaver saver = UnitTestLineSaver.create(measureSaver);
        
        FileLineCoverage coverageData= mock(FileLineCoverage.class);
        when(coverageData.getCountLines()).thenReturn(10);
        when(coverageData.getCoveredLines()).thenReturn(6);
        when(coverageData.getUncoveredLines()).thenReturn(4);
        File file = mock(File.class);
        saver.saveMeasures(coverageData, file);
        //Assert
        verify(measureSaver,times(1)).saveFileMeasure(any(Measure.class));
        verify(measureSaver,times(5)).saveFileMeasure(any(Metric.class),anyDouble());
        
    }
    private class IntegrationTestsCoverSensorStub extends IntegrationTestCoverSensor {
        public  IntegrationTestsCoverSensorStub(MsCoverProperties propertiesHelper,
                MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
            super(propertiesHelper, coverageHelperFactoryMock.getMock(),fileSystemMock.getMock(),microsoftWindowsEnvironment,measureSaverMock.getMock());
        }
    }
}
