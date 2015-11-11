package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;

public class OpenCoverSequencePointsObserverTest {

    private SonarCoverage registry;
    private  XmlParserSubject parser;
    
    @Before
    public void before() {
        registry = new SonarCoverage();
        parser = new OpenCoverParserSubject();
        OpenCoverObserver observer = new OpenCoverSequencePointsObserver();
        observer.setRegistry(registry);
        parser.registerObserver(observer);        
    }
    
    @Test
    public void ReadFileGeneratedBySpecFlow() {
        File file = TestUtils.getResource("OpenCoverSequencePoints/specflow.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals("Specflow files mess up the coverage info, file element is missing, should still parse",0,registry.size());
    }
    
    @Test
    public void ReadFileGeneratedBySpecFlowWithSomeData() {
        File file = TestUtils.getResource("OpenCoverSequencePoints/specflowcombined.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals("Specflow files mess up the coverage info, this one has one file",1,registry.size());
        SonarFileCoverage coverage=registry.getCoveredFile("1");
        CoverageLinePoints linePoints=coverage.getLinePoints();
        List<CoverageLinePoint> pointsList=linePoints.getPoints();
        CoverageLinePoint point;
        assertEquals("should have 11 points",11,pointsList.size());
    }
    
    @Test
    public void ReadFileWith11SequencePoint() {
        File file = TestUtils.getResource("OpenCoverSequencePoints/normal.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals("Should have one file",1,registry.size());
        SonarFileCoverage coverage=registry.getCoveredFile("1");
        CoverageLinePoints linePoints=coverage.getLinePoints();
        List<CoverageLinePoint> pointsList=linePoints.getPoints();
        CoverageLinePoint point;
        assertEquals("should have 11 points",11,pointsList.size());
    }

}
