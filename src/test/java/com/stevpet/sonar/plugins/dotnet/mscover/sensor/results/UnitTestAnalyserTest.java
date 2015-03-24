package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.NullResource;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.testutils.DummyFileSystem;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsAnalyser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Matchers.argThat;

public class UnitTestAnalyserTest {
    
    private Logger LOG = LoggerFactory.getLogger(UnitTestAnalyserTest.class);

    private Project project;
    private SensorContext context;
    private MeasureSaver measureSaver;
    private SourceFilePathHelper sourceFilePathHelper;
    private ResourceMediator resourceMediator;
    private ResourceSeam testResourceSeam;
    private DummyFileSystem fileSystem = new DummyFileSystem();

    @Before
    public void before() {
        project= mock(Project.class);
        when(project.getFileSystem()).thenReturn(new DummyFileSystem());
        measureSaver = mock(MeasureSaver.class);
        sourceFilePathHelper = new TestSourceFilePathHelper();
        resourceMediator=mock(ResourceMediator.class);
        testResourceSeam=mock(ResourceSeam.class);
        
    }
    @Test
    public void sunnyDay() {
        VsTestUnitTestResultsAnalyser analyser = new VsTestUnitTestResultsAnalyser(project, context,measureSaver) ;
        String coveragePath = TestUtils.getResource("Mileage/coverage.xml").getAbsolutePath();
        File resultsFile = TestUtils.getResource("Mileage/results.trx");
        String resultsPath=resultsFile.getAbsolutePath();
        analyser.analyseVsTestResults(coveragePath, resultsPath);
        verify(measureSaver,times(3)).saveSummaryMeasure(any(Metric.class),anyDouble());
    }
    
    @Test
    public void NoUnitTestsRecorded() {
        VsTestUnitTestResultsAnalyser analyser = new VsTestUnitTestResultsAnalyser(project, context,measureSaver) ;
        String coveragePath = TestUtils.getResource("Mileage/coverage.xml").getAbsolutePath();

        String resultsPath=StringUtils.EMPTY;
        analyser.analyseVsTestResults(coveragePath, resultsPath);
        verify(measureSaver,times(3)).saveSummaryMeasure(any(Metric.class),anyDouble());
    }
    
    @Test
    public void sunnyOpenCoverDay()  {
        String base="UnitTestAnalyser/OpenCover/";
        TestSeam testSeam = new TestSeam();
        VsTestUnitTestResultsAnalyser analyser = new VsTestUnitTestResultsAnalyser(project,measureSaver, sourceFilePathHelper,resourceMediator) ;
        when(resourceMediator.getSonarTestResource(any(File.class))).thenReturn(testSeam);
        String coveragePath = TestUtils.getResource(base+"coverage-report.xml").getAbsolutePath();
        File resultsFile = TestUtils.getResource(base + "testresults.trx");
        String resultsPath=resultsFile.getAbsolutePath();

        when(project.getFileSystem()).thenReturn(new DummyFileSystem());
        analyser.analyseOpenCoverTestResults(coveragePath, resultsPath);
        verify(measureSaver,times(3)).saveSummaryMeasure(any(Metric.class),anyDouble());
        assertEquals(4,testSeam.getSaveMeasureCnt());
        assertEquals(24,testSeam.getMetricValueCnt());

    }
    
    @Test(expected=SonarException.class)
    public void projectNotFound_ExpectSonarException() throws IOException {
        String base="UnitTestAnalyser/OpenCover/";
        TestSeam testSeam = new TestSeam();

        VsTestUnitTestResultsAnalyser analyser = new VsTestUnitTestResultsAnalyser(project,measureSaver, sourceFilePathHelper,resourceMediator) ;
        when(resourceMediator.getSonarTestResource(any(File.class))).thenReturn(testSeam);
        String coveragePath = TestUtils.getResource(base+"coverage-report.xml").getAbsolutePath();
        File resultsFile = TestUtils.getResource(base + "testresults.trx");
        String resultsPath=resultsFile.getAbsolutePath();


        givenProjectHasNoCanonicalPath();
        
        analyser.analyseOpenCoverTestResults(coveragePath, resultsPath);
        verify(measureSaver,times(3)).saveSummaryMeasure(any(Metric.class),anyDouble());
        assertEquals(4,testSeam.getSaveMeasureCnt());
        assertEquals(24,testSeam.getMetricValueCnt());
    }
    private void givenProjectHasNoCanonicalPath()
            throws IOException {
        File exceptionThrowingFile = mock(File.class);
        File baseDir = mock(File.class);
        when(exceptionThrowingFile.getCanonicalPath()).thenThrow(new IOException());
        fileSystem=mock(DummyFileSystem.class);
        when(fileSystem.getBasedir()).thenReturn(exceptionThrowingFile);
        when(project.getFileSystem()).thenReturn(fileSystem);
    }
    
    private class FileMatcher extends ArgumentMatcher<File> {

        @Override
        public boolean matches(Object argument) {
            // TODO Auto-generated method stub
            return false;
        }
        
    }
    private class TestSourceFilePathHelper extends SourceFilePathHelper {
        @SuppressWarnings("unused")
        @Override
        public File getCanonicalFile(String path) {
            return new File(path);
        }
    }
    
    private class TestSeam implements ResourceSeam {
        StringBuilder sb = new StringBuilder();
        private int saveMeasureCnt;
        private int saveMetricValueCnt;
 
        public void  saveResults() {
            File file = new File("testseam.txt");
            LOG.info("saving to " + file.getAbsolutePath());
            CharSequence data = sb.toString();
            try {
                FileUtils.write(file, data);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        public String getMetrics() {
            return sb.toString();
        }
        public void saveMetricValue(Metric metric, double value) {
            sb.append("metric ").append(metric.getKey()).append("=").append(value).append("\n");
            ++saveMetricValueCnt;
        }

        public int getMetricValueCnt() {
            return saveMetricValueCnt;
        }

        public int getSaveMeasureCnt() {
            return saveMeasureCnt;
        }

        public void saveMeasure(Measure measure) {
            ++saveMeasureCnt;
            sb.append("measure ").append(measure.getMetricKey()).append("=").append(measure.getData()).append("\n");
        }

        public boolean isIndexed(boolean acceptExcluded) {
            return true;
        }

        public String getLongName() {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isIncluded() {
            return true;
        }

        public void readSource(File file, String path, Charset charset) {
            // TODO Auto-generated method stub
            
        }

        public void setIsExcluded() {
            // TODO Auto-generated method stub
            
        }
        
    }
}
