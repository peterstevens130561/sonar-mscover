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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;


import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.File;
import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class OverallLineFileCoverageSaver implements
        LineFileCoverageSaver {
    private ResourceResolver resourceResolver;
    private SensorContext sensorContext;
    private final CoverageSaverHelper coverageSaverHelper = new DefaultCoverageSaverHelper();
    public OverallLineFileCoverageSaver(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    @Override
    public void setSensorContext(SensorContext sensorContext) {
        this.sensorContext = sensorContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.
     * LineFileCoverageSaver
     * #saveMeasures(com.stevpet.sonar.plugins.dotnet.mscover
     * .model.sonar.CoverageLinePoints, java.io.File)
     */
    @Override
    public void saveMeasures(
            CoverageLinePoints coveragePoints, java.io.File file) {
        Preconditions.checkState(sensorContext != null, "must call setSensorContext(sensorContext) first");
        File resource = resourceResolver.getFile(file);
        if (resource == null) {
            return;
        }
        SonarCoverageSummary summary = coveragePoints.getSummary();
        double coverage = summary.getCoverage();
        sensorContext.saveMeasure(resource, CoreMetrics.OVERALL_LINES_TO_COVER, (double) summary.getToCover());
        sensorContext.saveMeasure(resource, CoreMetrics.OVERALL_UNCOVERED_LINES,
                (double) summary.getToCover() - summary.getCovered());
        sensorContext.saveMeasure(resource, CoreMetrics.OVERALL_COVERAGE, coverageSaverHelper.convertPercentage(coverage));
        sensorContext.saveMeasure(resource, CoreMetrics.OVERALL_LINE_COVERAGE, coverageSaverHelper.convertPercentage(coverage));
        Measure<?> lineMeasures = coverageSaverHelper.getCoveredHitData(coveragePoints, CoreMetrics.OVERALL_COVERAGE_LINE_HITS_DATA);
        sensorContext.saveMeasure(resource, lineMeasures);
    }


}
