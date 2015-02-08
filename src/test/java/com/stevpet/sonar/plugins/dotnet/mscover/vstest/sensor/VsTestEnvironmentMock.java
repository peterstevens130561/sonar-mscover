package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
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
        verify(vsTestEnvironment,times(1)).setSonarCoverage(any(SonarCoverage.class));
        
    }

    public void givenSonarCoverage(SonarCoverage sonarCoverage) {
        when(vsTestEnvironment.getSonarCoverage()).thenReturn(sonarCoverage);
    }

}
