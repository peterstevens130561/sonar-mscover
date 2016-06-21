package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.File;
import org.sonar.api.utils.ParsingUtils;

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
     * Normal instantiation
     * @param resourceResolver
     * @param branchCoverageMetrics
     */
    public DefaultBranchCoverageSaver(ResourceResolver resourceResolver,BranchCoverageMetrics  branchCoverageMetrics) {
        this(resourceResolver,branchCoverageMetrics,new DefaultCoverageSaverHelper());
    }
    
    /**
     * For unit testing
     * 
     * @param resourceResolver
     * @param branchCoverageMetrics
     * @param coverageSaverHelper
     */
    public DefaultBranchCoverageSaver(ResourceResolver resourceResolver,BranchCoverageMetrics  branchCoverageMetrics, CoverageSaverHelper coverageSaverHelper) {
        this.resourceResolver = resourceResolver;
        this.branchCoverageMetrics = branchCoverageMetrics;
        this.coverageSaverHelper = coverageSaverHelper;
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
        sensorContext.saveMeasure(resource,branchCoverageMetrics.getCoverageMetric(),convertPercentage(summary.getCoverage()));
    }
    
    private double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }
    
    void saveLines( Metric<?> toCoverMetric, Metric<?> coveredMetric, CoverageLinePoints coveragePoints, File resource) {
        Measure<?> lineConditionsMeasure = coverageSaverHelper.getToCoverHitData(coveragePoints, toCoverMetric);
        sensorContext.saveMeasure(resource,lineConditionsMeasure);
        
        Measure<?> lineCoveredConditionsMeasure = coverageSaverHelper.getCoveredHitData(coveragePoints, coveredMetric);
        sensorContext.saveMeasure(resource,lineCoveredConditionsMeasure);
    }

}
