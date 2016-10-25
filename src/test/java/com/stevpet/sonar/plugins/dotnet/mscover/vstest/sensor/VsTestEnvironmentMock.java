/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VsTestEnvironmentMock {
    private VsTestEnvironment vsTestEnvironment = mock(VsTestEnvironment.class);

    public void givenTestsHaveExecuted() {
        when(vsTestEnvironment.getTestsHaveRun()).thenReturn(true);
    }

    public void givenTestsHaveNotExecuted() {
        when(vsTestEnvironment.getTestsHaveRun()).thenReturn(false);
    }

    public void verifyTestResultsPathIs(String resultsPath) {
        verify(vsTestEnvironment,times(1)).setTestResultsXmlPath(resultsPath);
    }

    public void verifyTestEnvironmentPathsNotSet() {
        verify(vsTestEnvironment,times(0)).setCoverageXmlPath(anyString());
        verify(vsTestEnvironment,times(0)).setTestResultsXmlPath(anyString());
    }

    public void verifyTestsHaveRun() {
        verify(vsTestEnvironment,times(1)).setTestsHaveRun();
    }
    
    public void verifyCoveragePathIs( String coveragePath) {
        verify(vsTestEnvironment,times(1)).setCoverageXmlPath(coveragePath);
    }
    
    public VsTestEnvironment getMock() {
        return vsTestEnvironment;
    }

    public void givenXmlCoveragePath(String string) {
        when(vsTestEnvironment.getXmlCoveragePath()).thenReturn(string);
    }

    public void verifySonarCoverageSet() {
        verify(vsTestEnvironment,times(1)).setSonarCoverage(any(DefaultProjectCoverageRepository.class));
        
    }

    public void givenSonarCoverage(DefaultProjectCoverageRepository sonarCoverage) {
        when(vsTestEnvironment.getSonarCoverage()).thenReturn(sonarCoverage);
    }

}
