package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

import org.mockito.Matchers;

public class UnitTestAnalyserTest {

    private Project project;
    private SensorContext context;

    @Before
    public void before() {
        project= mock(Project.class);
        when(project.getFileSystem()).thenReturn(new DummyFileSystem());
        context = mock(SensorContext.class);
        
    }
    @Test
    public void sunnyDay() {
        UnitTestAnalyser analyser = new UnitTestAnalyser(project, context) ;
        String coveragePath = TestUtils.getResource("Mileage/coverage.xml").getAbsolutePath();
        File resultsFile = TestUtils.getResource("Mileage/results.trx");
        String resultsPath=resultsFile.getAbsolutePath();
        analyser.analyseResults(coveragePath, resultsPath);
        verify(context,times(3)).saveMeasure(any(Metric.class),any(Double.class));
    }
}
