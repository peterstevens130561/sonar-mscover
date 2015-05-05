package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.when;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.model.ClassUnitTestResult;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.TestResultsFormatter;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.ProjectUnitTestResults;
import com.stevpet.sonar.plugins.dotnet.mscover.testresultssaver.TestResultsSaver;

public class DefaultTestResultsSaverTest {

    private TestResultsSaver testResultsSaver;
    @Mock
    private SensorContext sensorContext;
    @Mock
    private Project project;
    @Mock
    private TestResultsFormatter testResultsFormatter;
    @Mock
    private ResourceResolver resourceResolver;
    private ProjectUnitTestResults projectUnitTestResults;

    @Before()
    public void before() {
        MockitoAnnotations.initMocks(this);
        testResultsSaver = new DefaultTestResultsSaver(sensorContext,
                resourceResolver, testResultsFormatter);
        projectUnitTestResults = new ProjectUnitTestResults();
    }

    @Test
    public void noResults_nothingSaved() {
        testResultsSaver.save(projectUnitTestResults);
        verify(testResultsFormatter, times(0)).formatClassUnitTestResults(null);
    }

    @Test
    public void oneResults_oneFormatted() {
        projectUnitTestResults.addFile(new File("bogus"));
        when(resourceResolver.getFile(any(File.class))).thenReturn(
                org.sonar.api.resources.File.create("bogus"));
        testResultsSaver.save(projectUnitTestResults);
        verify(testResultsFormatter, times(1)).formatClassUnitTestResults(
                any(ClassUnitTestResult.class));
    }

    @Test
    public void oneResults_oneMeasureSaved() {
        projectUnitTestResults.addFile(new File("bogus"));
        when(resourceResolver.getFile(any(File.class))).thenReturn(
                org.sonar.api.resources.File.create("bogus"));
        testResultsSaver.save(projectUnitTestResults);
        verify(sensorContext, times(1)).saveMeasure(any(Resource.class),
                any(Measure.class));
    }

    @Test
    public void oneInvalidResults_noMeasureSaved() {
        projectUnitTestResults.addFile(null);
        when(resourceResolver.getFile(any(File.class))).thenReturn(
                org.sonar.api.resources.File.create("bogus"));
        testResultsSaver.save(projectUnitTestResults);
        verify(sensorContext, times(0)).saveMeasure(any(Resource.class),
                any(Measure.class));
    }

    @Test
    public void oneResults_metricsSavedSaved() {
        projectUnitTestResults.addFile(new File("bogus"));
        when(resourceResolver.getFile(any(File.class))).thenReturn(
                org.sonar.api.resources.File.create("bogus"));
        testResultsSaver.save(projectUnitTestResults);
        verify(sensorContext, times(6)).saveMeasure(any(Resource.class),
                any(Metric.class), anyDouble());
    }

    @Test
    public void twoResults_twoSaved() {
        projectUnitTestResults.addFiles("bogus", "bogus2");
        when(resourceResolver.getFile(any(File.class))).thenReturn(
                org.sonar.api.resources.File.create("bogus"));
        testResultsSaver.save(projectUnitTestResults);
        verify(testResultsFormatter, times(2)).formatClassUnitTestResults(
                any(ClassUnitTestResult.class));
    }

    @Test
    public void oneResults_notFound() {
        projectUnitTestResults = new ProjectUnitTestResults();
        projectUnitTestResults.addFile(new File("bogus"));
        when(resourceResolver.getFile(any(File.class))).thenReturn(null);
        testResultsSaver.save(projectUnitTestResults);
        verify(testResultsFormatter, times(0)).formatClassUnitTestResults(
                any(ClassUnitTestResult.class));
    }
}
