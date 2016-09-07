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
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;


import org.sonar.api.resources.File;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;

public class DefaultBranchCoverageSaver implements BranchFileCoverageSaver {
    private ResourceResolver resourceResolver;
    SensorContext sensorContext;
    private final BranchCoverageMetrics branchCoverageMetrics ;
    private final CoverageSaverHelper coverageSaverHelper;
    

    /**
     * 
     * @param resourceResolver
     * @param branchCoverageMetrics
     * @param coverageSaverHelper
     */
    public DefaultBranchCoverageSaver(ResourceResolver resourceResolver,BranchCoverageMetrics  branchCoverageMetrics,CoverageSaverHelper coverageSaverHelper) {
        this.resourceResolver = resourceResolver;
        this.branchCoverageMetrics = branchCoverageMetrics;
        this.coverageSaverHelper=coverageSaverHelper;

    }
    @Override
    public void setSensorContext(SensorContext sensorContext) {
        this.sensorContext = sensorContext;
    }
    @Override
    public void saveMeasures(CoverageLinePoints coveragePoints, java.io.File file) {
        Preconditions.checkState(sensorContext!=null,"must call setSensorContext(sensorContext) first");
        File resource = resourceResolver.getFile(file);
        if(resource==null) {
            return;
        }
        saveSummary(coveragePoints, resource);
        saveLines(branchCoverageMetrics.getToCoverByLineMetric(),branchCoverageMetrics.getCoveredByLineMetric(),coveragePoints, resource); 
    }
    
    private void saveSummary(CoverageLinePoints coveragePoints, File resource) {
        SonarCoverageSummary summary=coveragePoints.getSummary();
        sensorContext.saveMeasure(resource,branchCoverageMetrics.getUncoveredMetric(),(double) summary.getToCover()-summary.getCovered());
        sensorContext.saveMeasure(resource,branchCoverageMetrics.getToCoverMetric(),(double)summary.getToCover());
        sensorContext.saveMeasure(resource,branchCoverageMetrics.getCoverageMetric(),coverageSaverHelper.convertPercentage(summary.getCoverage()));
    }
    
    
    void saveLines( Metric<?> toCoverMetric, Metric<?> coveredMetric, CoverageLinePoints coveragePoints, File resource) {
        Measure<?> lineConditionsMeasure = coverageSaverHelper.getToCoverHitData(coveragePoints, toCoverMetric);
        sensorContext.saveMeasure(resource,lineConditionsMeasure);
        
        Measure<?> lineCoveredConditionsMeasure = coverageSaverHelper.getCoveredHitData(coveragePoints, coveredMetric);
        sensorContext.saveMeasure(resource,lineCoveredConditionsMeasure);
    }

}
