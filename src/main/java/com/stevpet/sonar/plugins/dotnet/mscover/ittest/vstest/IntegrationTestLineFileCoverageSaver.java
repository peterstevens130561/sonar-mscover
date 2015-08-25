package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.File;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarLinePoint;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.IntegrationTestResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class IntegrationTestLineFileCoverageSaver implements
		LineFileCoverageSaver {
	private ResourceResolver resourceResolver;
	private SensorContext sensorContext;

    public  IntegrationTestLineFileCoverageSaver(IntegrationTestResourceResolver resourceResolver,SensorContext sensorContext) {
        this.resourceResolver = resourceResolver;
        this.sensorContext = sensorContext;
    }
   
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.LineFileCoverageSaver#saveMeasures(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, java.io.File)
	 */
    @Override
	public void saveMeasures(
            CoverageLinePoints coveragePoints, java.io.File file) {
    	File resource = resourceResolver.getFile(file);
    	if(resource==null) {
    	    return;
    	}

        SonarCoverageSummary summary=coveragePoints.getSummary();
        double coverage = summary.getCoverage();
    	sensorContext.saveMeasure(resource, CoreMetrics.IT_LINES_TO_COVER, (double) summary.getToCover());
    	sensorContext.saveMeasure(resource, CoreMetrics.IT_UNCOVERED_LINES, (double)summary.getToCover() -summary.getCovered());
    	sensorContext.saveMeasure(resource,  CoreMetrics.IT_COVERAGE, convertPercentage(coverage));
    	sensorContext.saveMeasure(resource,  CoreMetrics.IT_LINE_COVERAGE, convertPercentage(coverage));
        Measure lineMeasures=getHitData(coveragePoints,CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
        sensorContext.saveMeasure(resource, lineMeasures);
    }

    /*
     * Generates a measure that contains the visits of each line of the source
     * file.
     */
    public Measure getHitData(CoverageLinePoints coveragePoints, Metric metric) {
        PropertiesBuilder<String, Integer> hitsBuilder =  new PropertiesBuilder<String, Integer>(metric);

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
