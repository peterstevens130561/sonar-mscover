/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.DefaultCoverageSaverFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

@DependsUpon( value={"IntegrationTestCoverageSaved","UnitTestCoverageSaved"})
public class OverallCoverageSensor implements Sensor {
    private static Logger LOG = LoggerFactory.getLogger(OverallCoverageSensor.class);
    private OverallCoverageCache coverageCache;
    private CoverageSaver coverageSaver;
    public OverallCoverageSensor(OverallCoverageCache coverageCache, CoverageSaver coverageSaver) {
        this.coverageCache=coverageCache;
        this.coverageSaver=coverageSaver;
    }
    
    public OverallCoverageSensor(OverallCoverageCache coverageCache, MicrosoftWindowsEnvironment microsoftWindowsEnvironment, PathResolver pathResolver, FileSystem fileSystem) {
        this(coverageCache,
                new DefaultCoverageSaverFactory(
                        microsoftWindowsEnvironment, 
                        pathResolver, 
                        fileSystem)
                .createOverallTestCoverageSaver()
                );
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return project.isModule();
    }

    @Override
    public void analyse(Project module, SensorContext context) {
        LOG.info("OveralCoverageSensor invoked");
        String moduleName=module.getName();
        SonarCoverage sonarCoverage = coverageCache.get(moduleName);
        if(sonarCoverage==null) {
            return;
        }
        coverageSaver.save(context, sonarCoverage);
        coverageCache.delete(moduleName);
    }

}
