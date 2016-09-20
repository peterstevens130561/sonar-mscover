/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.parser.opencover;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverSequencePointsObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarFileCoverage;

public class OpenCoverSequencePointsObserverParserTest {
    private ProjectCoverageRepository registry;
    private OpenCoverSequencePointsObserver observer;

    @Before()
    public void before() {
        XmlParser parser = new DefaultXmlParser();
        observer = new OpenCoverSequencePointsObserver();
        registry = new DefaultProjectCoverageRepository();
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
            SonarFileCoverage entry = registry.getCoverageOfFile("84");
            assertNotNull("file not saved",entry);
            CoverageLinePoints branchPoints = entry.getBranchPoints();
            assertEquals("one point",1,branchPoints.size());
            CoverageLinePoint branchPoint = branchPoints.getPoints().get(0);
            assertEquals("line",81,branchPoint.getLine());
            assertEquals("visited",1,branchPoint.getCovered());
        }
        
        @Test
        public void linePointEvents() {
            SonarFileCoverage entry = registry.getCoverageOfFile("84");
            assertNotNull("file not saved",entry);
            CoverageLinePoints points = entry.getLinePoints();
            assertEquals("one point",1,points.size());
            CoverageLinePoint point = points.getPoints().get(0);
            assertEquals("line",80,point.getLine());
            assertEquals("visited",1,point.getCovered());
        }
    }

