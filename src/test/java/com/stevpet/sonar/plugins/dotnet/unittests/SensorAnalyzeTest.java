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
package com.stevpet.sonar.plugins.dotnet.unittests;

import java.io.File;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.TestResultsBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.VsTestTestResultsSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestCache;
import com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor.OverallCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.utils.MsCoverTestUtils;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class SensorAnalyzeTest {

    @Mock
    private FileSystem fileSystem;
    @Mock private MsCoverConfiguration configuration;
    @Mock private UnitTestCache cache;
    @Mock private OpenCoverTestRunner runner;
    private OpenCoverUnitTestSensor sensor;
    @Mock private Project module;
    @Mock private SensorContext sensorContext;
    @Mock private TestResultsBuilder testResultsBuilder;
    @Mock private VsTestTestResultsSaver testResultsSaver;
    @Mock private CoverageReader coverageReader;
    @Mock private CoverageSaver coverageSaver;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    private File testResultsFile = new File("testResults");
    private File coverageFile = new File("workdir/coverage.xml");
    private ProjectUnitTestResults projectUnitTestResults = new ProjectUnitTestResults();
    @Mock private OverallCoverageRepository overallCoverageCache;
    @Mock private FileNamesParser fileNamesParser;

    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);

        sensor = new OpenCoverUnitTestSensor(fileSystem, configuration, cache,
                runner, testResultsBuilder, testResultsSaver, coverageReader,
                coverageSaver, microsoftWindowsEnvironment, overallCoverageCache,fileNamesParser);
        when(fileSystem.workDir()).thenReturn(new File("workdir"));
        when(runner.getTestResultsFile()).thenReturn(testResultsFile);
        when(cache.getTestCoverageFile()).thenReturn(coverageFile);
        when(cache.getTestResultsFile()).thenReturn(new File("testResults"));
        when(testResultsBuilder.getTestResults())
                .thenReturn(projectUnitTestResults);
    }


   
    @Test
    /**
     * see what happens if no unit test pattern is defined
     */
    public void runUnitTestsNoPattern() {
        when(configuration.getTestProjectPattern()).thenReturn(null);
        givenIsFirstProject();
        givenIsUnitTestProject();
        sensor.analyse(module, sensorContext);

        verify(runner, times(1)).setTestProjectPattern(new MsCoverTestUtils().isPattern(".*"));
    }
    
    

    public void runUnitTestsWithPattern() {
        Pattern pattern = Pattern.compile("SpecFlow");
        when(configuration.getTestProjectPattern()).thenReturn(pattern);
        givenIsFirstProject();
        givenIsUnitTestProject();
        sensor.analyse(module, sensorContext);

        verify(runner, times(1)).setTestProjectPattern(pattern);
    }

    @Test
    public void initialRunOnUnitTestProject() {
        Pattern pattern = Pattern.compile("SpecFlow");
        when(configuration.getTestProjectPattern()).thenReturn(pattern);
        givenIsFirstProject();
        givenIsUnitTestProject();
        sensor.analyse(module, sensorContext);

        shouldRunTests();
        shouldSaveCoverage();
        shouldSaveTestResults();
    }

    @Test
    public void initialRunOnProject() {
        givenIsNormalProject();
        givenIsFirstProject();

        // when
        sensor.analyse(module, sensorContext);

        shouldRunTests();
        shouldSaveCoverage();
        shouldNotSaveTestResults();
    }

    private void givenIsFirstProject() {
        when(cache.gatHasRun()).thenReturn(false);
    }

    private void shouldRunTests() {
        verify(runner, times(1)).execute();
    }

    @Test
    public void laterRunOnProject() {
        givenIsNormalProject();
        givenIsLaterProject();

        sensor.analyse(module, sensorContext);

        shouldNotRunTests();
        shouldSaveCoverage();
        shouldNotSaveTestResults();
    }

    private void givenIsLaterProject() {
        when(cache.gatHasRun()).thenReturn(true);
    }

    private void givenIsNormalProject() {
        when(microsoftWindowsEnvironment.isUnitTestProject(module)).thenReturn(
                false);
    }

    @Test
    public void laterRunOnUnitTestProject() {

        // given
        givenIsUnitTestProject();
        givenIsLaterProject();

        sensor.analyse(module, sensorContext);

        shouldNotRunTests();
        shouldSaveCoverage();
        shouldSaveTestResults();
    }

    private void givenIsUnitTestProject() {
        when(microsoftWindowsEnvironment.isUnitTestProject(eq(module),any(Pattern.class))).thenReturn(
                true);
    }

    private void shouldNotRunTests() {
        verify(runner, times(0)).execute();
    }

    private void shouldSaveTestResults() {
        verify(testResultsSaver, times(1)).save(sensorContext,
                projectUnitTestResults);
    }

    private void shouldNotSaveTestResults() {
        verify(testResultsSaver, times(0)).save(sensorContext,
                projectUnitTestResults);
    }

    private void shouldSaveCoverage() {
        verify(coverageSaver, times(1)).save(eq(sensorContext),
                any(DefaultProjectCoverageRepository.class));
    }

}
