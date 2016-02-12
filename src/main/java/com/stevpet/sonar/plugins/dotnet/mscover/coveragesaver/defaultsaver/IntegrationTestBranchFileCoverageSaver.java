package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;


import org.jfree.util.Log;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.File;
import org.sonar.api.utils.ParsingUtils;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class IntegrationTestBranchFileCoverageSaver implements
		BranchFileCoverageSaver {

	private ResourceResolver resourceResolver;
	private SensorContext sensorContext;

	IntegrationTestBranchFileCoverageSaver(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
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
        if(summary.getToCover()>0) {
            Log.info("something to cover") ;
        }
		sensorContext.saveMeasure(resource,CoreMetrics.IT_UNCOVERED_CONDITIONS,(double) summary.getToCover()-summary.getCovered());
		sensorContext.saveMeasure(resource,CoreMetrics.IT_CONDITIONS_TO_COVER,(double)summary.getToCover());
		sensorContext.saveMeasure(resource,CoreMetrics.IT_BRANCH_COVERAGE,convertPercentage(summary.getCoverage()));

	    PropertiesBuilder<String, Integer> lineConditionsBuilder = new PropertiesBuilder<String, Integer>(
	            CoreMetrics.IT_CONDITIONS_BY_LINE);
	    PropertiesBuilder<String, Integer> lineCoveredConditionsBuilder = new PropertiesBuilder<String, Integer>(
	            CoreMetrics.IT_COVERED_CONDITIONS_BY_LINE);
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
