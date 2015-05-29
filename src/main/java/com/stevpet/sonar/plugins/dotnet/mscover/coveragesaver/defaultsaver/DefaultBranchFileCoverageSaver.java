package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.File;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class DefaultBranchFileCoverageSaver implements BranchFileCoverageSaver {
	private ResourceResolver resourceResolver;
	private SensorContext sensorContext;
  
    public  DefaultBranchFileCoverageSaver(ResourceResolver resourceResolver,SensorContext sensorContext) {
        this.resourceResolver = resourceResolver;
        this.sensorContext = sensorContext;
    }
    
	@Override
	public void saveMeasures(CoverageLinePoints coveragePoints, java.io.File file) {
    	File resource = resourceResolver.getFile(file);
        SonarCoverageSummary summary=coveragePoints.getSummary();
		sensorContext.saveMeasure(resource,CoreMetrics.UNCOVERED_CONDITIONS,(double) summary.getToCover()-summary.getCovered());
		sensorContext.saveMeasure(resource,CoreMetrics.CONDITIONS_TO_COVER,(double)summary.getToCover());
		sensorContext.saveMeasure(resource,CoreMetrics.BRANCH_COVERAGE,convertPercentage(summary.getCoverage()));

	    PropertiesBuilder<String, Integer> lineConditionsBuilder = new PropertiesBuilder<String, Integer>(
	            CoreMetrics.CONDITIONS_BY_LINE);
	    PropertiesBuilder<String, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<String, Integer>(
	            CoreMetrics.COVERED_CONDITIONS_BY_LINE);
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