package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class OpenCoverSequencePointsObserverParserTest {
    private SonarCoverage registry;
    private OpenCoverSequencePointsObserver observer;

    @Before()
    public void before() {
        XmlParserSubject parser = new XmlParserSubject();
        observer = new OpenCoverSequencePointsObserver();
        registry = new SonarCoverage();
        observer.setRegistry(registry);
        parser.registerObserver(observer);
        File file = TestUtils.getResource("observers/OpenCoverSequencePointsObserver_minimal.xml");   
        assertNotNull("could not find file",file);
        parser.parseFile(file);
    }
    
        @Test
        public void sequencePointLine() {
            assertEquals("line",80,observer.getSequencePointLine());
        }
        
        @Test
        public void sequencePointVisited() {
            assertTrue("visited",observer.getSequencePointVisited());
        }
        
        @Test
        public void uid() {
            assertEquals("uid","84",observer.getMethodUid());
        }
        
        @Test
        public void branchPointLine() {
            assertEquals("line",81,observer.getBranchLine());
        }
        
        @Test
        public void branchPointVisited() {
            assertTrue("visited",observer.getBranchVisited());
        }
        
        @Test
        public void branchPointPath() {
            assertEquals("path",5,observer.getBranchPath());
        }
        
        @Test
        public void branchPointEvents() {
            SonarFileCoverage entry = registry.getCoveredFile("84");
            assertNotNull("file not saved",entry);
            CoverageLinePoints branchPoints = entry.getBranchPoints();
            assertEquals("one point",1,branchPoints.size());
            CoverageLinePoint branchPoint = branchPoints.getPoints().get(0);
            assertEquals("line",81,branchPoint.getLine());
            assertEquals("visited",1,branchPoint.getCovered());
        }
        
        @Test
        public void linePointEvents() {
            SonarFileCoverage entry = registry.getCoveredFile("84");
            assertNotNull("file not saved",entry);
            CoverageLinePoints points = entry.getLinePoints();
            assertEquals("one point",1,points.size());
            CoverageLinePoint point = points.getPoints().get(0);
            assertEquals("line",80,point.getLine());
            assertEquals("visited",1,point.getCovered());
        }
    }

