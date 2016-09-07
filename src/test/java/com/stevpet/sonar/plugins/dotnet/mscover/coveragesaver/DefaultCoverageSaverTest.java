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
    private BranchFileCoverageSaverMock branchFileCoverageSaverMock = new BranchFileCoverageSaverMock();
    private LineFileCoverageSaverMock lineFileCoverageSaverMock = new LineFileCoverageSaverMock();
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
        lineFileCoverageSaverMock.thenSaveMeasureCalled(1, SECOND_FILE);
        branchFileCoverageSaverMock.thenSaveMeasureCalled(1, SECOND_FILE);

    }

    @Test
    public void coverageWithTwoFiles_OneExcludedExcludedNotCalled() {
        injectSaverMocks();
        givenTwoCoveredFiles();
        givenExclude(FIRST_FILE);
        // No files
        whenSaverInvoked();
        // Then two saves are expected
        lineFileCoverageSaverMock.thenSaveMeasureCalled(0, FIRST_FILE);
        branchFileCoverageSaverMock.thenSaveMeasureCalled(0, FIRST_FILE);

    }

    private void thenSaveMeasureIsCalledTimes(int times) {
        lineFileCoverageSaverMock.thenSaveMeasureCalled(times);
        branchFileCoverageSaverMock.thenSaveMeasureCalled(times);
    }

    private void givenExclude(String path) {
        testFiles.add(new File(path));
    }

    private void whenSaverInvoked() {
        saver.save(coverage);
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
        container.addComponent(branchFileCoverageSaverMock.getMock());
        container.addComponent(lineFileCoverageSaverMock.getMock());
        saver = container.getComponent(CoverageSaverBase.class);
    }

}
