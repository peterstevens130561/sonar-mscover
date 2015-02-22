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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.saver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverageMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceLine;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;

public class IntegrationTestLineSaver_SaveMeasuresTest {
    MeasureSaverMock measureSaverMock = new MeasureSaverMock();
    FileLineCoverageMock fileLineCoverageMock = new FileLineCoverageMock();
    IntegrationTestLineSaver integrationTestLineSaver = IntegrationTestLineSaver.create(measureSaverMock.getMock());
    private File file;
    
    @Test
    public void SimpleSave_shouldSaveAllParameters() {
        String name="bogus.cs";
        int lines=200;
        int uncovered=50;
        int covered=150;
        double coverage=0.75;
        Map<Integer, SourceLine> lineMap = new HashMap<Integer,SourceLine>();
        SourceLine value = new SourceLine(10);
        value.addVisits(4);
        lineMap.put(1, value);
        
        file= new File(name);
        fileLineCoverageMock.givenCountLines(lines)
        .givenUnCoveredLines(uncovered)
        .givenCoveredLines(covered)
        .givenCoverage(coverage)
        .givenLines(lineMap);
        
        //when
        integrationTestLineSaver.saveMeasures(fileLineCoverageMock.getMock(), file);
        
        //then
        measureSaverMock.verifySaveFileMeasure(CoreMetrics.IT_LINES_TO_COVER,lines);
        measureSaverMock.verifySaveFileMeasure(CoreMetrics.IT_UNCOVERED_LINES,uncovered);
        measureSaverMock.verifySaveFileMeasure(CoreMetrics.IT_COVERAGE,coverage*100);
        
        PropertiesBuilder<String, Integer> lineHitsBuilder = new PropertiesBuilder<String, Integer>(
                CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
        Measure lineHits=lineHitsBuilder.add("10", 4).build().setPersistenceMode(PersistenceMode.DATABASE);
        measureSaverMock.verifySaveFileMeasure(lineHits);       
    }

    
}
