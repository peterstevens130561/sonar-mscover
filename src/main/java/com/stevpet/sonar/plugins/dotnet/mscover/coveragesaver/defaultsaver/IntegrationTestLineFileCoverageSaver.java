package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.File;
import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.LineFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverageSummary;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.IntegrationTestResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class IntegrationTestLineFileCoverageSaver implements
		LineFileCoverageSaver {
	private ResourceResolver resourceResolver;
	private SensorContext sensorContext;
	private CoverageSaverHelper coverageSaverHelper = new DefaultCoverageSaverHelper();

	@Deprecated
    public  IntegrationTestLineFileCoverageSaver(IntegrationTestResourceResolver resourceResolver,SensorContext sensorContext) {
        this.resourceResolver = resourceResolver;
        this.sensorContext = sensorContext;
    }
    public  IntegrationTestLineFileCoverageSaver(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }
    
    @Override
    public void setSensorContext(SensorContext sensorContext) {
    	this.sensorContext = sensorContext;
    }
    /* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.opencover.saver.LineFileCoverageSaver#saveMeasures(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoverageLinePoints, java.io.File)
	 */
    @Override
	public void saveMeasures(
            CoverageLinePoints coveragePoints, java.io.File file) {
		Preconditions.checkState(sensorContext!=null,"must call setSensorContext(sensorContext) first");
    	File resource = resourceResolver.getFile(file);
    	if(resource==null) {
    	    return;
    	}

        SonarCoverageSummary summary=coveragePoints.getSummary();
        double coverage = summary.getCoverage();
    	sensorContext.saveMeasure(resource, CoreMetrics.IT_LINES_TO_COVER, (double) summary.getToCover());
    	sensorContext.saveMeasure(resource, CoreMetrics.IT_UNCOVERED_LINES, (double)summary.getToCover() -summary.getCovered());
    	sensorContext.saveMeasure(resource,  CoreMetrics.IT_COVERAGE, coverageSaverHelper.convertPercentage(coverage));
    	sensorContext.saveMeasure(resource,  CoreMetrics.IT_LINE_COVERAGE, coverageSaverHelper.convertPercentage(coverage));
        Measure<?> lineMeasures=coverageSaverHelper.getCoveredHitData(coveragePoints,CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
        sensorContext.saveMeasure(resource, lineMeasures);
    }

    
}
