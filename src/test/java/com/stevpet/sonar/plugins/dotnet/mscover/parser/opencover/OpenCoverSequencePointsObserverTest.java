/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class OpenCoverSequencePointsObserverTest {

    private ProjectCoverageRepository registry;
    private  XmlParser parser;
    
    @Before
    public void before() {
        registry = new DefaultProjectCoverageRepository();
        parser = new DefaultXmlParser();
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
        SonarFileCoverage coverage=registry.getCoverageOfFile("1");
        CoverageLinePoints linePoints=coverage.getLinePoints();
        List<CoverageLinePoint> pointsList=linePoints.getPoints();

        assertEquals("should have 11 points",11,pointsList.size());
    }
    
    @Test
    public void ReadFileWith11SequencePoint() {
        File file = TestUtils.getResource("OpenCoverSequencePoints/normal.xml");
        //Act
        parser.parseFile(file);
        //Assert
        assertEquals("Should have one file",1,registry.size());
        SonarFileCoverage coverage=registry.getCoverageOfFile("1");
        CoverageLinePoints linePoints=coverage.getLinePoints();
        List<CoverageLinePoint> pointsList=linePoints.getPoints();
        assertEquals("should have 11 points",11,pointsList.size());
    }

}
