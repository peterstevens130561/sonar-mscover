package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;


import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class IntegrationTestBranchFileCoverageSaver extends ItemCoverageSaverBase implements
		BranchFileCoverageSaver {


	IntegrationTestBranchFileCoverageSaver(ResourceResolver resourceResolver) {
	    super(resourceResolver);
    }

	@Override
    protected Metric getUncoveredMetric() {
        return CoreMetrics.IT_UNCOVERED_CONDITIONS;
    }
	
    @Override
    protected Metric getToCoverMetric() {
        return CoreMetrics.IT_CONDITIONS_TO_COVER;
    }
    
    @Override
    protected Metric getCoverageMetric() {
        return CoreMetrics.IT_BRANCH_COVERAGE;
    }
    
    @Override
    protected Metric getToCoverByLineMetric() {
        return CoreMetrics.IT_CONDITIONS_BY_LINE;
    }
    
    @Override
    protected Metric getCoveredByLineMetric() {
        return CoreMetrics.IT_COVERED_CONDITIONS_BY_LINE;
    }
   

}
