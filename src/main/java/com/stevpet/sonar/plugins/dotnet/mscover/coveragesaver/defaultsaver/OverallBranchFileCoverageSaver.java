package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

import com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.BranchFileCoverageSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;

public class OverallBranchFileCoverageSaver extends ItemCoverageSaverBase implements
        BranchFileCoverageSaver {

    OverallBranchFileCoverageSaver(ResourceResolver resourceResolver) {
        super(resourceResolver);
    }

    @Override
    protected Metric getUncoveredMetric() {
        return CoreMetrics.OVERALL_UNCOVERED_CONDITIONS;
    }

    @Override
    protected Metric getToCoverMetric() {
        return CoreMetrics.OVERALL_CONDITIONS_TO_COVER;
    }

    @Override
    protected Metric getCoverageMetric() {
        return CoreMetrics.OVERALL_BRANCH_COVERAGE;
    }

    @Override
    protected Metric getToCoverByLineMetric() {
        return CoreMetrics.OVERALL_CONDITIONS_BY_LINE;
    }

    @Override
    protected Metric getCoveredByLineMetric() {
        return CoreMetrics.OVERALL_COVERED_CONDITIONS_BY_LINE;
    }

}