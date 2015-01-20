package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsAnalyser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;

public class UnitTestAnalyserTest {
    
    private Logger LOG = LoggerFactory.getLogger(UnitTestAnalyserTest.class);

    private Project project;
    private MeasureSaver measureSaver;
    private ResourceMediator resourceMediator;
    FileSystemMock fileSystemMock = new FileSystemMock();

    @Before
    public void before() {
        project= mock(Project.class);
        measureSaver = mock(MeasureSaver.class);
        resourceMediator=mock(ResourceMediator.class);
    }
    
    /**
     * As of 4.2.1 compatibility we no longer save summary measures
     */
    @Test
    public void sunnyDay() {

        fileSystemMock.givenDefaultEncoding();
        VsTestUnitTestResultsAnalyser analyser = createResultsAnalyser();
        String coveragePath = TestUtils.getResource("Mileage/coverage.xml").getAbsolutePath();
        File resultsFile = TestUtils.getResource("Mileage/results.trx");
        fileSystemMock.givenBaseDir(resultsFile.getParentFile());
        
        String resultsPath=resultsFile.getAbsolutePath();
        analyser.analyseVsTestResults(coveragePath, resultsPath);
        verify(measureSaver,times(0)).saveSummaryMeasure(any(Metric.class),anyDouble());
    }
    
    /**
     * As of 4.2.1 compatibility we no longer save summary measures
     */
    @Test
    public void sunnyOpenCoverDay()  {
        String base="UnitTestAnalyser/OpenCover/";
        //TestSeam testSeam = mock(TestSeam.class);
        VsTestUnitTestResultsAnalyser analyser = createResultsAnalyser();
        //when(resourceMediator.getSonarTestResource(any(File.class))).thenReturn(testSeam);
        String coveragePath = TestUtils.getResource(base+"coverage-report.xml").getAbsolutePath();
        File resultsFile = TestUtils.getResource(base + "testresults.trx");
        String resultsPath=resultsFile.getAbsolutePath();

        analyser.analyseOpenCoverTestResults(coveragePath, resultsPath);
        //TODO: UNITTEST Need to fix to make it a real unit test again...
        

    }

    private VsTestUnitTestResultsAnalyser createResultsAnalyser() {
        VsTestUnitTestResultsAnalyser analyser = new VsTestUnitTestResultsAnalyser(project, measureSaver,mock(SourceFilePathHelper.class),resourceMediator,fileSystemMock.getMock()) ;
        return analyser;
    }

    
    @Test(expected=SonarException.class)
    public void projectNotFound_ExpectSonarException() throws IOException {
        String base="UnitTestAnalyser/OpenCover/";
        TestSeam testSeam = new TestSeam();

        VsTestUnitTestResultsAnalyser analyser = createResultsAnalyser();
        when(resourceMediator.getSonarTestResource(any(File.class))).thenReturn(testSeam);
        String coveragePath = TestUtils.getResource(base+"coverage-report.xml").getAbsolutePath();
        File resultsFile = TestUtils.getResource(base + "testresults.trx");
        String resultsPath=resultsFile.getAbsolutePath();


        givenProjectHasNoCanonicalPath();
        
        analyser.analyseOpenCoverTestResults(coveragePath, resultsPath);
        verify(measureSaver,times(0)).saveSummaryMeasure(any(Metric.class),anyDouble());
        assertEquals(0,testSeam.getSaveMeasureCnt());
        assertEquals(0,testSeam.getMetricValueCnt());
    }
    
    private void givenProjectHasNoCanonicalPath()
            throws IOException {
        File exceptionThrowingFile = mock(File.class);
        when(exceptionThrowingFile.getCanonicalPath()).thenThrow(new IOException());
        fileSystemMock.givenBaseDir(exceptionThrowingFile);
    }
    
    private class TestSourceFilePathHelper extends SourceFilePathHelper {
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
