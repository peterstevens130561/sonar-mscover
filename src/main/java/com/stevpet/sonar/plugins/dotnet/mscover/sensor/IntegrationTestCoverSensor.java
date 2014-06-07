/*
 * Sonar .NET Plugin :: MsCover
 * Copyright (C) 2014 Peter Stevens
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
package com.stevpet.sonar.plugins.dotnet.mscover.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.IntegrationTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
@Extension
public class IntegrationTestCoverSensor implements Sensor {

    static final Logger LOG = LoggerFactory
            .getLogger(IntegrationTestCoverSensor.class);

    private final PropertiesHelper propertiesHelper ;
    private TimeMachine timeMachine;
    private CoverageHelper coverageHelper;
    /**
     * Use of IoC to get Settings
     */
    public IntegrationTestCoverSensor(Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            TimeMachine timeMachine) {
        propertiesHelper = new PropertiesHelper(settings);
        this.timeMachine=timeMachine;
        coverageHelper = CoverageHelper.create(propertiesHelper,microsoftWindowsEnvironment);
    }
    

    public boolean shouldExecuteOnProject(Project project) {
        return coverageHelper.shouldExecuteOnProject(project) && propertiesHelper.isIntegrationTestsEnabled() ;
    }

    public void analyse(Project project, SensorContext sensorContext) {
        ResourceMediator resourceMediator = ResourceMediator.createWithFilters(sensorContext, project, timeMachine, propertiesHelper);
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);

        String coveragePath=propertiesHelper.getIntegrationTestsPath();
        coverageHelper.prepareIntegration(measureSaver);
        coverageHelper.analyse(project,coveragePath);
    }

}
