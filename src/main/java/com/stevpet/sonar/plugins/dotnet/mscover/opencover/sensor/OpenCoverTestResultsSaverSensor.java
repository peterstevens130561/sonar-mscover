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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.AbstractDotNetSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsAnalyser;
@DependsUpon("OpenCoverRunningVsTest")
public class OpenCoverTestResultsSaverSensor extends AbstractDotNetSensor {

    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageResultsSensor.class);
    private MsCoverProperties propertiesHelper;
    private VsTestEnvironment vsTestEnvironment;
    private VsTestUnitTestResultsAnalyser analyser = new VsTestUnitTestResultsAnalyser();
    private FileSystem fileSystem;
    private ResourceMediator resourceMediator;

    public OpenCoverTestResultsSaverSensor(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            MsCoverProperties propertiesHelper,
            VsTestEnvironment vsTestEnvironment,
            FileSystem fileSystem,
            ResourceMediator resourceMediator) {
        super(microsoftWindowsEnvironment, "OpenCover", propertiesHelper.getMode());
        this.propertiesHelper = propertiesHelper;
        this.vsTestEnvironment=vsTestEnvironment;
        this.fileSystem = fileSystem;
        this.resourceMediator=resourceMediator;
    }

    @Override
    public String[] getSupportedLanguages() {
        return new String[] {"cs"};
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        if(!super.shouldExecuteOnProject(project)) {
            return false;
        }
        if(!propertiesHelper.runOpenCover()) {
            return false;
        }

        LOG.info("Will execute OpenCoverTestResultsSaverSensor for" + project.getName());
        return true;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        if(!vsTestEnvironment.getTestsHaveRun()) {
            LOG.info("Will not execute OpenCoverage test results sensor, as tests have not run");
            return;
        }
        LOG.info("Saving test results of " + project.getName()  );       
        
        MeasureSaver measureSaver = SonarMeasureSaver.create(project,sensorContext,resourceMediator);
        analyser.setMeasureSaver(measureSaver);
        analyser.setResourceMediator(resourceMediator) ;
        analyser.setFileSystem(fileSystem);
            
        String coveragePath = vsTestEnvironment.getXmlCoveragePath();
        String resultsPath = vsTestEnvironment.getXmlResultsPath();
        analyser.analyseOpenCoverTestResults(coveragePath, resultsPath);
    }
}
