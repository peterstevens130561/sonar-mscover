package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;

public class SonarCoverageSaverTest {
    
    private SensorContext sensorContext;
    private Project project;
    private SonarCoverage sonarCoverageRegistry;
    private MeasureSaver measureSaver;
    @Before
    public void before() {
        
    }
    
    @Test
    public void create_ExpectObject() {
        SonarCoverageSaver coverageSaver = new SonarCoverageSaver(measureSaver);
        assertNotNull(coverageSaver);
    }
    
    @Test
    public void simpleSave() {
        measureSaver = mock(MeasureSaver.class);
        SonarCoverageSaver coverageSaver = new SonarCoverageSaver(measureSaver);
        sonarCoverageRegistry = new SonarCoverage();
        coverageSaver.setCoverageRegistry(sonarCoverageRegistry);

        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(sonarCoverageRegistry);
        File file = TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);   

        
        coverageSaver.setCoverageRegistry(sonarCoverageRegistry);
        coverageSaver.save();
        verify(measureSaver,times(93)).saveFileMeasure(any(Measure.class));
        verify(measureSaver,times(62)).setFile(any(File.class)); 
        
        verify(measureSaver,times(0)).saveFileMeasure(eq(CoreMetrics.LINES), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.LINES_TO_COVER), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.CONDITIONS_TO_COVER), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.UNCOVERED_CONDITIONS), anyDouble());
    }
    
    @Test
    public void simpleSaveWithExclusion() {
        measureSaver = mock(MeasureSaver.class);
        SonarCoverageSaver coverageSaver = new SonarCoverageSaver(measureSaver);
        sonarCoverageRegistry = new SonarCoverage();
        coverageSaver.setCoverageRegistry(sonarCoverageRegistry);

        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        XmlParserSubject parser = parserFactory.createOpenCoverParser(sonarCoverageRegistry);
        //Given parsed file coverage=report.xml
        File file = TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);    
        coverageSaver.setCoverageRegistry(sonarCoverageRegistry);
        //Given I exclude file
        List<File> testFiles = new ArrayList<File>();
        testFiles.add(new File("c:/Development/Jewel.Release.Oahu/JewelEarth/Core/ThinClient/WinForms/ViewHost.cs"));
        coverageSaver.setExcludeSourceFiles(testFiles);
        //When I save
        coverageSaver.save();
        //Then
        verify(measureSaver,times(90)).saveFileMeasure(any(Measure.class));
        verify(measureSaver,times(60)).setFile(any(File.class)); 
        
        verify(measureSaver,times(0)).saveFileMeasure(eq(CoreMetrics.LINES), anyDouble());
        verify(measureSaver,times(30)).saveFileMeasure(eq(CoreMetrics.LINES_TO_COVER), anyDouble());
        verify(measureSaver,times(30)).saveFileMeasure(eq(CoreMetrics.CONDITIONS_TO_COVER), anyDouble());
        verify(measureSaver,times(30)).saveFileMeasure(eq(CoreMetrics.UNCOVERED_CONDITIONS), anyDouble());
    }
}
