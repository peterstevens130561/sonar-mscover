package com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.ConcreteOpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.SonarCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.interfaces.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.SonarMeasureSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ConcreteParserFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.ParserFactory;

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
        SonarCoverageSaver coverageSaver = new SonarCoverageSaver(sensorContext,project,measureSaver);
        assertNotNull(coverageSaver);
    }
    
    @Test
    public void simpleSave() {
        measureSaver = mock(MeasureSaver.class);
        SonarCoverageSaver coverageSaver = new SonarCoverageSaver(sensorContext,project, measureSaver);
        sonarCoverageRegistry = new SonarCoverage();
        coverageSaver.setCoverageRegistry(sonarCoverageRegistry);

        OpenCoverParserFactory parserFactory = new ConcreteOpenCoverParserFactory();
        ParserSubject parser = parserFactory.createOpenCoverParser(sonarCoverageRegistry);
        File file = TestUtils.getResource("coverage-report.xml");
        parser.parseFile(file);   

        
        coverageSaver.setCoverageRegistry(sonarCoverageRegistry);
        coverageSaver.save();
        verify(measureSaver,times(93)).saveFileMeasure(any(Measure.class));
        verify(measureSaver,times(62)).setFile(any(File.class)); 
        
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.LINES), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.LINES_TO_COVER), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.CONDITIONS_TO_COVER), anyDouble());
        verify(measureSaver,times(31)).saveFileMeasure(eq(CoreMetrics.UNCOVERED_CONDITIONS), anyDouble());
    }
}
