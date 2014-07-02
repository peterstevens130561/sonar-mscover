package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.*;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.ParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;

public class SourceFileNamesObserverTest {

    private SonarCoverage registry;
    File file = TestUtils.getResource("coverage-report.xml");
    
    @Test
    public void ReadFileNames_Expect31() {
        //Arrange
        ParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals(31,registry.size());
        assertNull(registry.getCoveredFile("5").getAbsolutePath()); // is not included
        assertNotNull(registry.getCoveredFile("86").getAbsolutePath()); // is included
    }


    @Test
    public void ReadPoints_ExpectMany() {
        initializeCompleteParser();
        
        SonarFileCoverage coverage=registry.getCoveredFile("8");
        assertEquals("line points",15,coverage.getLinePoints().size());
        assertEquals("branch points",4,coverage.getBranchPoints().size());

        coverage=registry.getCoveredFile("11");
        assertEquals("line points",48,coverage.getLinePoints().size());
        assertEquals("branch points",4,coverage.getBranchPoints().size()); 
    }

    @Test
    public void ReadPoints_CheckLineSummaryFile8() {
        initializeCompleteParser();
        SonarFileCoverage coverage=registry.getCoveredFile("8");
        SonarCoverageSummary summaryCoverage=coverage.getLinePoints().getSummary();
        assertEquals(15,summaryCoverage.getToCover()); 
        assertEquals(0,summaryCoverage.getCovered());
    }
    
    @Test
    public void ReadPoints_CheckLineSummaryFile11() {
        initializeCompleteParser();
        SonarFileCoverage coverage=registry.getCoveredFile("11");
        SonarCoverageSummary summaryCoverage=coverage.getLinePoints().getSummary();
        assertEquals(48,summaryCoverage.getToCover()); 
        assertEquals(29,summaryCoverage.getCovered());
    }
    
    @Test
    public void ReadPoints_CheckBranchSummaryFile8() {
        initializeCompleteParser();
        SonarFileCoverage coverage=registry.getCoveredFile("8");
        SonarCoverageSummary summaryCoverage=coverage.getBranchPoints().getSummary();
        assertEquals(12,summaryCoverage.getToCover()); 
        assertEquals(0,summaryCoverage.getCovered());
    }
    
    @Test
    public void ReadPoints_CheckBranchSummaryFile11() {
        initializeCompleteParser();
        SonarFileCoverage coverage=registry.getCoveredFile("11");
        SonarCoverageSummary summaryCoverage=coverage.getBranchPoints().getSummary();
        assertEquals(15,summaryCoverage.getToCover()); 
        assertEquals(5,summaryCoverage.getCovered());
    }
    
    private void initializeCompleteParser() {
        ParserSubject parser = initializeParser();
        SequencePointsObserver pointsObserver = new SequencePointsObserver();
        pointsObserver.setRegistry(registry);
        parser.registerObserver(pointsObserver);
        parser.parseFile(file);
    }
    
    private ParserSubject initializeParser() {
        ParserSubject parser = new OpenCoverParserSubject();
        SourceFileNamesObserver observer = new SourceFileNamesObserver();
        registry = new SonarCoverage();
        observer.setRegistry(registry);
        parser.registerObserver(observer);
        return parser;
    }
    

}
