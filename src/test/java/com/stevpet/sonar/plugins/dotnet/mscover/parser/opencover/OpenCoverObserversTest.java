/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverSourceFileNamesObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class OpenCoverObserversTest {

    private SonarCoverage registry;
    private File file = TestUtils.getResource("coverage-report.xml");
    
    @Test
    public void ReadFileNames_Expect31() {
        //Arrange
        XmlParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals(31,registry.size());
        assertNull(registry.getCoveredFile("5").getAbsolutePath()); // is not included
        assertNotNull(registry.getCoveredFile("86").getAbsolutePath()); // is included
    }


    @Test
    public void readTwice() {
        XmlParserSubject parser = initializeParser();
        File file = TestUtils.getResource("coverage-report.xml");
        //Act
        parser.parseFile(file);  
        file = TestUtils.getResource("coverage-report.xml");
        assertTrue("should be able to write this file",file.canWrite());
    }

    @Test
    public void ReadPoints_ExpectMany() {
        initializeCompleteParser();       
        checkCoverageParsingResults(registry); 
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
        XmlParserSubject parser = initializeParser();
        OpenCoverObserver pointsObserver = new OpenCoverSequencePointsObserver();
        pointsObserver.setRegistry(registry);
        parser.registerObserver(pointsObserver);
        parser.parseFile(file);
    }
    
    private XmlParserSubject initializeParser() {
        XmlParserSubject parser = new XmlParserSubject();
        OpenCoverObserver observer = new OpenCoverSourceFileNamesObserver();
        registry = new SonarCoverage();
        observer.setRegistry(registry);
        parser.registerObserver(observer);
        return parser;
    }
  
    public static void checkCoverageParsingResults(SonarCoverage registry) {
        int fileCount=registry.getValues().size();
        assertEquals("files",31,fileCount);
        
        SonarFileCoverage coverage=registry.getCoveredFile("8");
        assertEquals("line points",15,coverage.getLinePoints().size());
        assertEquals("branch points",6,coverage.getBranchPoints().size());

        coverage=registry.getCoveredFile("11");
        assertEquals("line points",48,coverage.getLinePoints().size());
        assertEquals("branch points",1,coverage.getBranchPoints().size());
    }
    

}
