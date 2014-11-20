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
import org.sonar.api.resources.Project;
import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.DefaultResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
@Extension
public class IntegrationTestCoverSensor implements Sensor {

    static final Logger LOG = LoggerFactory
            .getLogger(IntegrationTestCoverSensor.class);

    private final MsCoverProperties propertiesHelper ;
    private TimeMachine timeMachine;
    private CoverageHelper coverageHelper;
    private AbstractCoverageHelperFactory coverageHelperFactory ;
    private ResourceMediatorFactory resourceMediatorFactory = new DefaultResourceMediatorFactory();
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    private ShouldExecuteHelper shouldExecuteHelper;
    /**
     * Use of IoC to get Settings
     */
    public IntegrationTestCoverSensor(MsCoverProperties propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            TimeMachine timeMachine) {
        this.propertiesHelper = propertiesHelper;
        this.timeMachine=timeMachine;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment ;
        this.coverageHelperFactory = new SonarCoverageHelperFactory();
        shouldExecuteHelper = coverageHelperFactory.createShouldExecuteHelper(propertiesHelper);
    }
    

    public boolean shouldExecuteOnProject(Project project) {
        return shouldExecuteHelper.shouldExecuteOnProject(project) && propertiesHelper.isIntegrationTestsEnabled() ;
    }

    public void analyse(Project project, SensorContext sensorContext) {

        ResourceMediator resourceMediator = resourceMediatorFactory.createWithFilters(sensorContext, project, timeMachine, propertiesHelper);
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);
        coverageHelper = coverageHelperFactory.createIntegrationTestCoverageHelper(propertiesHelper, microsoftWindowsEnvironment, measureSaver);
        String coveragePath=propertiesHelper.getIntegrationTestsPath();
        coverageHelper.analyse(project,coveragePath);
    }

}
