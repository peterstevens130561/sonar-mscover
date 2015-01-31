package com.stevpet.sonar.plugins.dotnet.mscover.sensor.results;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFilePathHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestUnitTestResultsAnalyser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;

public class UnitTestAnalyserTest {
   
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
        VsTestUnitTestResultsAnalyser analyser = createResultsAnalyser();
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

  
}
