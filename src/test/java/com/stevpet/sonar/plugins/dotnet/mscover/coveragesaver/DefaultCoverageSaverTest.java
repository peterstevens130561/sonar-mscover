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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.batch.SensorContext;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver.CoverageSaverBase;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultCoverageSaverTest {
    private static final String SECOND_FILE = "b/c";
    private static final String FIRST_FILE = "a/b";
    private DefaultPicoContainer container = new DefaultPicoContainer();
    private SonarCoverage coverage = new SonarCoverage();
    private CoverageSaver saver;
    private List<File> testFiles;
    @Mock
    private BranchFileCoverageSaver branchCoverageSaver;
    @Mock
    private LineFileCoverageSaver lineCoverageSaver;
    @Mock
    private SensorContext sensorContext;
    @Mock
    private ResourceResolver resourceResolver;
    @Mock
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        container.addComponent(CoverageSaverBase.class)
                .addComponent(sensorContext).addComponent(resourceResolver).addComponent(microsoftWindowsEnvironment);
        testFiles = new ArrayList<File>();
        when(microsoftWindowsEnvironment.getUnitTestSourceFiles()).thenReturn(testFiles);
    }



    @Test
    public void coverageWithTwoFiles_CalledTwice() {
        injectSaverMocks();
        givenTwoCoveredFiles();
        whenSaverInvoked();
        thenSaveMeasureIsCalledTimes(2);
    }

    @Test
    public void coverageWithNoFiles_CalledNone() {
        injectSaverMocks();
        // No files
        whenSaverInvoked();
        // Then two saves are expected
        thenSaveMeasureIsCalledTimes(0);
    }

    @Test
    public void coverageWithTwoFiles_OneExcluded() {
        injectSaverMocks();
        givenTwoCoveredFiles();
        givenExclude(FIRST_FILE);
        // No files
        whenSaverInvoked();
        // Then two saves are expected
        verify(lineCoverageSaver,times(1)).saveMeasures(eq(sensorContext),eq(new File(SECOND_FILE)),any());
        verify(branchCoverageSaver,times(1)).saveMeasures(eq(sensorContext),eq(new File(SECOND_FILE)),any());
    }

    @Test
    public void coverageWithTwoFiles_OneExcludedExcludedNotCalled() {
        injectSaverMocks();
        givenTwoCoveredFiles();
        givenExclude(FIRST_FILE);
        // No files
        whenSaverInvoked();
        // Then two saves are expected
        verify(lineCoverageSaver,times(1)).saveMeasures(eq(sensorContext),eq(new File(SECOND_FILE)),any());
        verify(branchCoverageSaver,times(1)).saveMeasures(eq(sensorContext),eq(new File(SECOND_FILE)),any());
        verify(lineCoverageSaver,times(0)).saveMeasures(eq(sensorContext),eq(new File(FIRST_FILE)),any());
        verify(branchCoverageSaver,times(0)).saveMeasures(eq(sensorContext),eq(new File(FIRST_FILE)),any());
    }

    private void thenSaveMeasureIsCalledTimes(int times) {
        verify(lineCoverageSaver,times(times)).saveMeasures(eq(sensorContext), any(),any());
        verify(branchCoverageSaver,times(times)).saveMeasures(eq(sensorContext), any(),any());;
    }

    private void givenExclude(String path) {
        testFiles.add(new File(path));
    }

    private void whenSaverInvoked() {
        saver.save(sensorContext, coverage);
    }

    private void givenTwoCoveredFiles() {
        SonarFileCoverage fileCoverage = coverage.getCoveredFile("0");
        fileCoverage.addBranchPoint(1, true);
        fileCoverage.addBranchPoint(2, true);
        fileCoverage.setAbsolutePath(FIRST_FILE);

        fileCoverage = coverage.getCoveredFile("1");
        fileCoverage.addLinePoint(3, true);
        fileCoverage.addLinePoint(4, true);
        fileCoverage.setAbsolutePath(SECOND_FILE);
    }

    private void injectSaverMocks() {
        saver = new CoverageSaverBase(branchCoverageSaver, lineCoverageSaver, microsoftWindowsEnvironment);
    }

}
