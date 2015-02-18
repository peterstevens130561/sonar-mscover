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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver;

import java.io.File;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver;

public class SonarBranchSaver implements FileCoverageSaver {

   private MeasureSaver measureSaver;
    
    private final PropertiesBuilder<String, Integer> lineConditionsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.CONDITIONS_BY_LINE);
    private final PropertiesBuilder<String, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.COVERED_CONDITIONS_BY_LINE);
    
    private SonarBranchSaver(MeasureSaver measureSaver) {
        this.measureSaver = measureSaver ;
    }
    
    public static FileCoverageSaver create(MeasureSaver measureSaver) {
        return new SonarBranchSaver(measureSaver);
    }
    
    public void saveMeasures(CoverageLinePoints coveragePoints, File file) {
        measureSaver.setFile(file);
        SonarCoverageSummary summary=coveragePoints.getSummary();
        measureSaver.saveFileMeasure(CoreMetrics.UNCOVERED_CONDITIONS,(double) summary.getToCover()-summary.getCovered());
        measureSaver.saveFileMeasure(CoreMetrics.CONDITIONS_TO_COVER,(double)summary.getToCover());
        measureSaver.saveFileMeasure(CoreMetrics.BRANCH_COVERAGE,convertPercentage(summary.getCoverage()));

        lineConditionsBuilder.clear();
        lineCoveredConditionsBuilder.clear();
        for (CoverageLinePoint linePoint : coveragePoints.getPoints()) {
            int lineNumber = linePoint.getLine();
            lineConditionsBuilder.add(Integer.toString(lineNumber), linePoint.getToCover());
            lineCoveredConditionsBuilder.add(Integer.toString(lineNumber), linePoint.getCovered());
        }
        Measure lineConditionsMeasure= lineConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        measureSaver.saveFileMeasure(lineConditionsMeasure);
        Measure lineCoveredConditionsMeasure=lineCoveredConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        measureSaver.saveFileMeasure(lineCoveredConditionsMeasure); 
    }
    
    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }
}
