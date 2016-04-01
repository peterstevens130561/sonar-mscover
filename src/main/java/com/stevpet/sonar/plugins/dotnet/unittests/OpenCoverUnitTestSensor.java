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
package com.stevpet.sonar.plugins.dotnet.unittests;

import java.io.File;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.OpenCoverUnitTestCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.defaulttestresultsbuilder.SpecFlowTestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestCache;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache;
import com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor.OverallCoverageCache;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

/**
 * ProjectBuilder for dotnet projects
 * 
 * The build method will be invoked by sonar in the ProjectBuild phase, and
 * populates the MicrosoftWindowsEnvironment
 * 
 * @author stevpet
 * 
 */
@DependedUpon("UnitTestCoverageSaved")
public class OpenCoverUnitTestSensor implements Sensor {

    private Logger Log = LoggerFactory.getLogger(OpenCoverUnitTestSensor.class);
    private MsCoverConfiguration configuration;
    private TestCache cache;
    private OpenCoverTestRunner testRunner;
    private FileSystem fileSystem;
    private CoverageReader reader;
    private CoverageSaver coverageSaver;
    private TestResultsBuilder testResultsBuilder;
    private VsTestTestResultsSaver testResultsSaver;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private OverallCoverageCache overallCoverageCache;

    /**
     * Includes all dependencies
     * 
     * @param fileSystem
     * @param configuration
     * @param unitTestBatchData
     * @param testRunner
     * @param testResultsBuilder
     * @param testResultsSaver
     * @param coverageReader
     * @param coverageSaver
     * @param microsoftWindowsEnvironment
     */
    public OpenCoverUnitTestSensor(FileSystem fileSystem,
            MsCoverConfiguration configuration,
            UnitTestCache unitTestBatchData, OpenCoverTestRunner testRunner,
            TestResultsBuilder testResultsBuilder,
            VsTestTestResultsSaver testResultsSaver,
            CoverageReader coverageReader, CoverageSaver coverageSaver,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            OverallCoverageCache overallCoverageCache) {
        this.fileSystem = fileSystem;
        this.configuration = configuration;
        this.cache = unitTestBatchData;
        this.testRunner = testRunner;
        this.testResultsBuilder = testResultsBuilder;
        this.testResultsSaver = testResultsSaver;
        this.reader = coverageReader;
        this.coverageSaver = coverageSaver;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.overallCoverageCache = overallCoverageCache;
        ;
    }

    /**
     * For export in plugin, uses the standard defaults
     * 
     * @param fileSystem
     * @param msCoverConfiguration
     * @param unitTestBatchData
     * @param coverageSaver
     * @param microsoftWindowsEnvironment
     * @param pathResolver
     * @param vsTestEnvironment
     */
    public OpenCoverUnitTestSensor(FileSystem fileSystem,
            MsCoverConfiguration msCoverConfiguration,
            Settings settings,
            UnitTestCache unitTestBatchData,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            PathResolver pathResolver,
            OverallCoverageCache overallCoverageCache) {
        this(fileSystem, msCoverConfiguration, unitTestBatchData,
                DefaultOpenCoverTestRunner.create(msCoverConfiguration,settings,
                        microsoftWindowsEnvironment, fileSystem), SpecFlowTestResultsBuilder
                        .create(microsoftWindowsEnvironment),
                VsTestTestResultsSaver.create(pathResolver, fileSystem),
                new OpenCoverCoverageReader(msCoverConfiguration),
                new OpenCoverUnitTestCoverageSaver(microsoftWindowsEnvironment,
                        pathResolver, fileSystem), microsoftWindowsEnvironment,overallCoverageCache);
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return project.isModule() && configuration.runOpenCover()
                && microsoftWindowsEnvironment.hasUnitTestSourceFiles();
    }

    @Override
    public void analyse(Project project, SensorContext context) {
        File testResultsFile;
        File coverageFile;
        SonarCoverage sonarCoverage;
        Pattern pattern = getTestProjectPattern();
        Log.debug("project {}", project.getName());
        if (!cache.gatHasRun()) {
            coverageFile = new File(fileSystem.workDir(), "coverage.xml");
            testRunner.setCoverageFile(coverageFile);
            testRunner.onlyReportAssembliesOfTheSolution();

            testRunner.setTestProjectPattern(pattern);
            testRunner.execute();

            testResultsFile = testRunner.getTestResultsFile();

            sonarCoverage = new SonarCoverage();
            reader.read(sonarCoverage, coverageFile);
            cache.setSonarCoverage(sonarCoverage);
            cache.setHasRun(coverageFile, testResultsFile);
        } else {
            sonarCoverage = cache.getSonarCoverage();
            testResultsFile = cache.getTestResultsFile();
            coverageFile = cache.getTestCoverageFile();
        }
        overallCoverageCache.merge(sonarCoverage, project.getName());
        coverageSaver.save(context, sonarCoverage);

        if (testResultsFile != null
                && microsoftWindowsEnvironment.isUnitTestProject(project,pattern)) {
            ProjectUnitTestResults testResults = testResultsBuilder.parse(
                    testResultsFile, coverageFile);
            Log.debug("test results read {}", testResults.getTests());
            testResultsSaver.save(context, testResults);
        }
    }

    private Pattern getTestProjectPattern() {
        Pattern pattern = configuration.getTestProjectPattern();
        if (pattern==null) {
            pattern = Pattern.compile(".*");
        }
        return pattern;
    }
}