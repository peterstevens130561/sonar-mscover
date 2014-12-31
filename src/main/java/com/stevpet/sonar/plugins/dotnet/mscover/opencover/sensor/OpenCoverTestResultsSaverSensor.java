/*
 * SonarQube MSCover coverage plugin
 * Copyright (C) 2014 Peter Stevens
 * peter@famstevens.eu
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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.DefaultResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.AbstractBaseSensor;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsAnalyser;
@DependsUpon("OpenCoverRunningVsTest")
public class OpenCoverTestResultsSaverSensor extends AbstractBaseSensor {

    private static final Logger LOG = LoggerFactory.getLogger(OpenCoverCoverageResultsSensor.class);
    private MsCoverProperties propertiesHelper;
    private VsTestEnvironment vsTestEnvironment;
    private TimeMachine timeMachine;
    private ResourceMediatorFactory resourceMediatorFactory = new DefaultResourceMediatorFactory();
    private VsTestUnitTestResultsAnalyser analyser = new VsTestUnitTestResultsAnalyser();
    public OpenCoverTestResultsSaverSensor(
            MsCoverProperties propertiesHelper,
            VsTestEnvironment vsTestEnvironment,
            TimeMachine timeMachine) {
        super( "OpenCover", propertiesHelper.getMode());
        this.propertiesHelper = propertiesHelper;
        this.vsTestEnvironment=vsTestEnvironment;
        this.timeMachine = timeMachine;
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
        if(!propertiesHelper.isTestProject(project)) {
            return false;
        }
        if(!propertiesHelper.runOpenCover()) {
            return false;
        }
        if(!vsTestEnvironment.getTestsHaveRun()) {
            LOG.info("Will not execute OpenCoverage results sensor, as tests have not run");
            return false;
        }
        LOG.info("Will execute " + project.getName());
        return true;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        LOG.info("Saving test results of " + project.getName()  );
        ResourceMediator resourceMediator = resourceMediatorFactory.createWithFilters(sensorContext,project,timeMachine,propertiesHelper);            
        
        MeasureSaver measureSaver = SonarMeasureSaver.create(sensorContext,resourceMediator);
        analyser.setProject(project);
        analyser.setMeasureSaver(measureSaver);
        analyser.setResourceMediator(resourceMediator) ;
        
        String coveragePath = vsTestEnvironment.getXmlCoveragePath();
        String resultsPath = vsTestEnvironment.getXmlResultsPath();
        analyser.analyseOpenCoverTestResults(coveragePath, resultsPath);
    }
}
