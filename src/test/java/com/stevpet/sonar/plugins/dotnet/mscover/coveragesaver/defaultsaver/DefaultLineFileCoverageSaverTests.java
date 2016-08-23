package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.SensorContext;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultLineFileCoverageSaverTests {
    private LineFileCoverageSaver lineFileCoverageSaver ;
    private LineCoverageMetrics lineCoverageMetrics;
    @Mock private ResourceResolver resourceResolver;
    @Mock private CoverageSaverHelper coverageSaverHelper;
    @Mock private CoverageLinePoints coveragePoints;
    @Mock private File file;
    @Mock private SensorContext sensorContext;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        lineCoverageMetrics = new OverallLineCoverageMetrics(); // makes no sense to mock
        lineFileCoverageSaver = new DefaultLineFileCoverageSaver(lineCoverageMetrics, resourceResolver, coverageSaverHelper);
    }
    
    @Test
    public void normal() {
        lineFileCoverageSaver.setSensorContext(sensorContext);
        lineFileCoverageSaver.saveMeasures(coveragePoints, file);
    }
    
}
