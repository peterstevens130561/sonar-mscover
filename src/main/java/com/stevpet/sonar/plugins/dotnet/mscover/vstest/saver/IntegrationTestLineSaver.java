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

import java.util.Map;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileLineCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceLine;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class IntegrationTestLineSaver implements LineMeasureSaver {
    
    private MeasureSaver measureSaver;


    private IntegrationTestLineSaver(MeasureSaver measureSaver) {
        this.measureSaver = measureSaver ;
    }
    
    public static IntegrationTestLineSaver create(MeasureSaver measureSaver) {
        return new IntegrationTestLineSaver(measureSaver);
    }

    private final PropertiesBuilder<String, Integer> lineHitsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
    

    public void saveMeasures(FileLineCoverage coverageData, java.io.File file) {

        double coverage = coverageData.getCoverage();
        measureSaver.setFile(file);
        measureSaver.saveFileMeasure(CoreMetrics.IT_LINES_TO_COVER,(double) coverageData.getCountLines());

        measureSaver.saveFileMeasure(CoreMetrics.IT_UNCOVERED_LINES,(double) coverageData.getCountLines()
                        - coverageData.getCoveredLines());
        measureSaver.saveFileMeasure(CoreMetrics.IT_COVERAGE,convertPercentage(coverage));
        measureSaver.saveFileMeasure(CoreMetrics.IT_LINE_COVERAGE,convertPercentage(coverage));
        Measure lineMeasures=getHitData(coverageData);
        measureSaver.saveFileMeasure(lineMeasures);
    }

    /*
     * Generates a measure that contains the visits of each line of the source
     * file.
     */
    public Measure getHitData(FileLineCoverage coverable) {
        PropertiesBuilder<String, Integer> hitsBuilder =  lineHitsBuilder;

        hitsBuilder.clear();
        Map<Integer, SourceLine> lines = coverable.getLines();
        for (SourceLine line : lines.values()) {
            int lineNumber = line.getLineNumber();
            int countVisits = line.getVisits();
            hitsBuilder.add(Integer.toString(lineNumber), countVisits);
        }
        return hitsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
    }
    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }




}
