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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.DotNetConstants;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.AbstractCoverageHelperFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sensor.SonarCoverageHelperFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;


@DependsUpon({VsTestExecutionSensor.DEPENDS,DotNetConstants.CORE_PLUGIN_EXECUTED})
public class VsTestUnitTestResultsSensor implements Sensor {;
    static final Logger LOG = LoggerFactory
            .getLogger(VsTestUnitTestResultsSensor.class);
    private MsCoverProperties propertiesHelper ;
    private VsTestRunner unitTestRunner;
    private VsTestEnvironment vsTestEnvironment;
    private VsTestUnitTestResultsAnalyser vsTestUnitTestResultsAnalyser = new VsTestUnitTestResultsAnalyser();
    private FileSystem fileSystem;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private ResourceMediator resourceMediator;
    
    public VsTestUnitTestResultsSensor( MsCoverProperties propertiesHelper,
            VsTestEnvironment vsTestEnvironment,
            FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,ResourceMediator resourceMediator) {

        this.propertiesHelper = propertiesHelper;
        unitTestRunner = new WindowsVsTestRunner(propertiesHelper,microsoftWindowsEnvironment,fileSystem);

        this.vsTestEnvironment = vsTestEnvironment;
        this.fileSystem = fileSystem;
        this.microsoftWindowsEnvironment= microsoftWindowsEnvironment;
        this.resourceMediator=resourceMediator;
    }
    
    /**
     * tests whether the sensor should execute on the project
     */
    public boolean shouldExecuteOnProject(Project project) {

        if(propertiesHelper.getRunMode() == RunMode.SKIP) {
            return false;
        }
        String resultsPath=propertiesHelper.getUnitTestResultsPath();
        boolean resultsDefined=StringUtils.isNotEmpty(resultsPath);
        
        boolean rightLevel = project.isRoot() == propertiesHelper.excuteRoot();
        boolean shouldRunUnitTests=propertiesHelper.runVsTest();
        boolean shouldExecute = (resultsDefined || shouldRunUnitTests) && rightLevel;
        LOG.info("ResultsSensor {}",shouldExecute);
        return shouldExecute;
    }

    public void analyse(Project project, SensorContext sensorContext) {
        LOG.info("MsCover Starting analysing test results");
        String coveragePath;
        String resultsPath;        
        
        MeasureSaver measureSaver = SonarMeasureSaver.create(project,sensorContext,resourceMediator);

        vsTestUnitTestResultsAnalyser.setMeasureSaver(measureSaver);
        vsTestUnitTestResultsAnalyser.setResourceMediator(resourceMediator) ;
        
        if(propertiesHelper.runVsTest()) {
            coveragePath=vsTestEnvironment.getXmlCoveragePath();
            resultsPath=vsTestEnvironment.getXmlResultsPath();
        } else {
            LOG.info("MsCover using test results");
            coveragePath = propertiesHelper.getUnitTestCoveragePath();
            resultsPath=propertiesHelper.getUnitTestResultsPath();
        }
        vsTestUnitTestResultsAnalyser.analyseVsTestResults(coveragePath, resultsPath);
        if(propertiesHelper.runVsTest()) {
            AbstractCoverageHelperFactory coverageHelperFactory = new SonarCoverageHelperFactory();
            CoverageSaver coverageHelper = coverageHelperFactory.createUnitTestCoverageHelper(fileSystem, measureSaver);
            List<String> modules = microsoftWindowsEnvironment.getCurrentSolution().getArtifactNames();
            coverageHelper.analyse(project,coveragePath,modules);
        }
    }


    public void setVsTestUnitTestResultsAnalyser(
            VsTestUnitTestResultsAnalyser mock) {
        this.vsTestUnitTestResultsAnalyser = mock;
    }

    

}
