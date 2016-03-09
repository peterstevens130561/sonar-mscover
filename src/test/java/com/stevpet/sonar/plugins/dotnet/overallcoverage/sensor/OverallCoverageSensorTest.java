package com.stevpet.sonar.plugins.dotnet.overallcoverage.sensor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.CoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class OverallCoverageSensorTest {

    private Sensor sensor;
    @Mock private Project module;
    @Mock private SensorContext context;
    @Mock private CoverageCache coverageCache;
    @Mock private CoverageSaver coverageSaver;

    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        sensor = new OverallCoverageSensor(coverageCache,coverageSaver);
    }
    
    @Test
    public void testAnalyse() {
        SonarCoverage sonarCoverage = new SonarCoverage();
        when(coverageCache.get(module)).thenReturn(sonarCoverage);
        sensor.analyse(module, context);
        verify(coverageCache,times(1)).get(module);
        verify(coverageSaver,times(1)).save(context, sonarCoverage);
    }

}
