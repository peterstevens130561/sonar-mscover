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

import org.sonar.plugins.dotnet.api.microsoft.MicrosoftWindowsEnvironment;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.UnitTestBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.plugin.Extension;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.CoverageRegistry;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.Saver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.line.UnitTestLineSaver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
@Extension
public class UnitTestCoverSensor extends BaseCoverageSensor {


    /**
     * Use of IoC to get Settings
     */
    public UnitTestCoverSensor(Settings settings,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            TimeMachine timeMachine) {
        super(settings,microsoftWindowsEnvironment,timeMachine);
    }

    @Override
    protected String getCoveragePath() {
        return  new PropertiesHelper(settings).getUnitTestCoveragePath();
    }

    @Override
    protected Saver createLineSaver(Project project, SensorContext sensorContext,
            CoverageRegistry registry) {
        return new UnitTestLineSaver(sensorContext, project,
                registry);
    }
    
    @Override
    protected BaseBlockSaver createBlockSaver(Project project, SensorContext sensorContext) {
        return new UnitTestBlockSaver(sensorContext, project);
    }

    @Override
    protected boolean shouldExecuteSensor(PropertiesHelper helper) {
        return helper.isUnitTestsEnabled();
    }

}
