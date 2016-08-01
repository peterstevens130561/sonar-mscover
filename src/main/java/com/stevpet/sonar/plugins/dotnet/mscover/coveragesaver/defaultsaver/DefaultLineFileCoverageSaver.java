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

public class DefaultLineFileCoverageSaver implements LineFileCoverageSaver
{
    
    private final LineCoverageMetrics lineCoverageMetrics;
    private final ResourceResolver resourceResolver;
    private final CoverageSaverHelper coverageSaverHelper;
    private SensorContext sensorContext;

    public DefaultLineFileCoverageSaver(LineCoverageMetrics lineCoverageMetrics,ResourceResolver resourceResolver,CoverageSaverHelper coverageSaverHelper) {
        this.lineCoverageMetrics = lineCoverageMetrics;
        this.resourceResolver = resourceResolver;
        this.coverageSaverHelper = coverageSaverHelper;
    }

    @Override
    public void setSensorContext(SensorContext sensorContext) {
        this.sensorContext=sensorContext;
    }

    @Override
    public void saveMeasures(CoverageLinePoints coveragePoints, java.io.File file) {
        Preconditions.checkState(sensorContext!=null,"must call setSensorContext(sensorContext) first");
        File resource = resourceResolver.getFile(file);
        if(resource==null) {
            return;
        }

        SonarCoverageSummary summary=coveragePoints.getSummary();
        double coverage = summary.getCoverage();
        sensorContext.saveMeasure(resource, lineCoverageMetrics.getLinesToCoverMetric(), (double) summary.getToCover());
        sensorContext.saveMeasure(resource, lineCoverageMetrics.getUncoveredLinesMetric(), (double)summary.getToCover() -summary.getCovered());
        sensorContext.saveMeasure(resource,  CoreMetrics.COVERAGE, coverageSaverHelper.convertPercentage(coverage));
        sensorContext.saveMeasure(resource,  CoreMetrics.LINE_COVERAGE, coverageSaverHelper.convertPercentage(coverage));
        Measure<?> lineMeasures=coverageSaverHelper.getCoveredHitData( coveragePoints,CoreMetrics.COVERAGE_LINE_HITS_DATA);
        sensorContext.saveMeasure(resource, lineMeasures); 
    }

}
