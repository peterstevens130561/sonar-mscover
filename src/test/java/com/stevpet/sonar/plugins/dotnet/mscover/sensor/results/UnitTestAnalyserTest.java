package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;

public class UnitTestAnalyserTest {

    private Project project;
    private SensorContext context;
    private MeasureSaver measureSaver;

    @Before
    public void before() {
        project= mock(Project.class);
        when(project.getFileSystem()).thenReturn(new DummyFileSystem());
        measureSaver = mock(MeasureSaver.class);
        
    }
    @Test
    public void sunnyDay() {
        UnitTestAnalyser analyser = new UnitTestAnalyser(project, context,measureSaver) ;
        String coveragePath = TestUtils.getResource("Mileage/coverage.xml").getAbsolutePath();
        File resultsFile = TestUtils.getResource("Mileage/results.trx");
        String resultsPath=resultsFile.getAbsolutePath();
        analyser.analyseResults(coveragePath, resultsPath);
        verify(measureSaver,times(3)).saveSummaryMeasure(any(Metric.class),anyDouble());
    }
}
