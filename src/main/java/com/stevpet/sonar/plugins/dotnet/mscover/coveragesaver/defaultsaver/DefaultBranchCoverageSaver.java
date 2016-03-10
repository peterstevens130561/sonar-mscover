package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.File;
import org.sonar.api.utils.ParsingUtils;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.FileCoverageSaver;

public class DefaultBranchCoverageSaver implements BranchFileCoverageSaver {
    private ResourceResolver resourceResolver;
    private SensorContext sensorContext;
    private final BranchCoverageMetrics branchCoverageMetrics ;
    public DefaultBranchCoverageSaver(ResourceResolver resourceResolver,BranchCoverageMetrics  branchCoverageMetrics) {
        this.resourceResolver = resourceResolver;
        this.branchCoverageMetrics = branchCoverageMetrics;
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
        SonarCoverageSummary summary=coveragePoints.getSummary();
        sensorContext.saveMeasure(resource,branchCoverageMetrics.getUncoveredMetric(),(double) summary.getToCover()-summary.getCovered());
        sensorContext.saveMeasure(resource,branchCoverageMetrics.getToCoverMetric(),(double)summary.getToCover());
        sensorContext.saveMeasure(resource,branchCoverageMetrics.getCoverageMetric(),convertPercentage(summary.getCoverage()));

        PropertiesBuilder<String, Integer> lineConditionsBuilder = new PropertiesBuilder<String, Integer>(
                branchCoverageMetrics.getToCoverByLineMetric());
        PropertiesBuilder<String, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<String, Integer>(
                branchCoverageMetrics.getCoveredByLineMetric());
        lineConditionsBuilder.clear();
        lineCoveredConditionsBuilder.clear();
        for (CoverageLinePoint linePoint : coveragePoints.getPoints()) {
            int lineNumber = linePoint.getLine();
            lineConditionsBuilder.add(Integer.toString(lineNumber), linePoint.getToCover());
            lineCoveredConditionsBuilder.add(Integer.toString(lineNumber), linePoint.getCovered());
        }
        Measure lineConditionsMeasure= lineConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        sensorContext.saveMeasure(resource,lineConditionsMeasure);
        Measure lineCoveredConditionsMeasure=lineCoveredConditionsBuilder.build().setPersistenceMode(PersistenceMode.DATABASE);
        sensorContext.saveMeasure(resource,lineCoveredConditionsMeasure); 
    }
    
   
    private double convertPercentage(Number percentage) {
        return ParsingUtils.scaleValue(percentage.doubleValue() * 100.0);
    }

}
