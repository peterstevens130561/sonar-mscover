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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;


import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.File;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultLineFileCoverageSaver implements  LineFileCoverageSaver {

	private ResourceResolver resourceResolver;
	private SensorContext sensorContext;

    public  DefaultLineFileCoverageSaver(ResourceResolver resourceResolver,SensorContext sensorContext) {
        this.resourceResolver = resourceResolver;
        this.sensorContext = sensorContext;
    }
    

    private final PropertiesBuilder<String, Integer> lineHitsBuilder = new PropertiesBuilder<String, Integer>(
            CoreMetrics.COVERAGE_LINE_HITS_DATA);
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.sensor.opencover.FileCoverageSaver#saveMeasures(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, java.io.File)
     */
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.LineFileCoverageSaver#saveMeasures(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, java.io.File)
	 */
    @Override
	public void saveMeasures(
            CoverageLinePoints coveragePoints, java.io.File file) {
    	File resource = resourceResolver.getFile(file);


        SonarCoverageSummary summary=coveragePoints.getSummary();
        double coverage = summary.getCoverage();
    	sensorContext.saveMeasure(resource, CoreMetrics.LINES_TO_COVER, (double) summary.getToCover());
    	sensorContext.saveMeasure(resource, CoreMetrics.UNCOVERED_LINES, (double)summary.getToCover() -summary.getCovered());
    	sensorContext.saveMeasure(resource,  CoreMetrics.COVERAGE, convertPercentage(coverage));
    	sensorContext.saveMeasure(resource,  CoreMetrics.LINE_COVERAGE, convertPercentage(coverage));
        Measure lineMeasures=getHitData(coveragePoints);
        sensorContext.saveMeasure(resource, lineMeasures);
    }

    /*
     * Generates a measure that contains the visits of each line of the source
     * file.
     */
    public Measure getHitData(CoverageLinePoints coveragePoints) {
        PropertiesBuilder<String, Integer> hitsBuilder =  lineHitsBuilder;

        hitsBuilder.clear();
        for (CoveragePoint point : coveragePoints.getPoints()) {
            int lineNumber = ((SonarLinePoint) point).getLine();
            int countVisits = point.getCovered();
            hitsBuilder.add(Integer.toString(lineNumber), countVisits);
        }
        return hitsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
    }
    
    protected double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }
    

}

