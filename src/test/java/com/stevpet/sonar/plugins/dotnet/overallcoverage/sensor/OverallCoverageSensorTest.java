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

    private static final String MODULE_NAME = "bla";
    private Sensor sensor;
    @Mock private Project module;
    @Mock private SensorContext context;
    @Mock private CoverageCache coverageCache;
    @Mock private CoverageSaver coverageSaver;

    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        sensor = new OverallCoverageSensor(coverageCache,coverageSaver);
        when(module.getName()).thenReturn(MODULE_NAME);
    }
    
    @Test
    public void testAnalyse() {
        SonarCoverage sonarCoverage = new SonarCoverage();
        when(coverageCache.get(MODULE_NAME)).thenReturn(sonarCoverage);
        sensor.analyse(module, context);
        verify(coverageCache,times(1)).get(MODULE_NAME);
        verify(coverageSaver,times(1)).save(context, sonarCoverage);
    }
    
    @Test
    public void testNoCoverage() {
        when(coverageCache.get(MODULE_NAME)).thenReturn(null);
        sensor.analyse(module, context);
        verify(coverageCache,times(1)).get(MODULE_NAME);
        verify(coverageSaver,times(0)).save(context, null);
    }

}
