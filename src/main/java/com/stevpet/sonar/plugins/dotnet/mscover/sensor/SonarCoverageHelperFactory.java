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

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.IntegrationTestLineSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.LineMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver.UnitTestLineSaver;
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class SonarCoverageHelperFactory implements BatchExtension,
        AbstractCoverageHelperFactory {

    private VSTestCoverageSaver coverageHelper ;

    public CoverageSaver createVsTestIntegrationTestCoverageHelper(
  
            FileSystem fileSystem,
            MeasureSaver measureSaver) {
        createVsTestCoverageHelper(fileSystem);
        LineMeasureSaver lineSaver=IntegrationTestLineSaver.create(measureSaver);
        injectSavers(lineSaver);
        return coverageHelper;
        
    }

    public CoverageSaver createUnitTestCoverageHelper(
            FileSystem fileSystem,
            MeasureSaver measureSaver) {
            createVsTestCoverageHelper(fileSystem);
            LineMeasureSaver lineSaver=UnitTestLineSaver.create(measureSaver);
            injectSavers(lineSaver);
            return coverageHelper;       
    }
    

   private void createVsTestCoverageHelper(
            FileSystem fileSystem) {
        coverageHelper = new VSTestCoverageSaver(fileSystem);
    }


    private void injectSavers(LineMeasureSaver lineSaver) {
        coverageHelper.setLineSaver(lineSaver);
    }

    public  ShouldExecuteHelper createShouldExecuteHelper(
            MsCoverProperties propertiesHelper) {
        return new SonarShouldExecuteHelper(propertiesHelper);
    }

}
