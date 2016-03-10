package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

public class OverallBranchCoverageMetrics implements BranchCoverageMetrics {

    @Override
    public Metric getUncoveredMetric() {
        return CoreMetrics.OVERALL_UNCOVERED_CONDITIONS;
    }

    @Override
    public Metric getToCoverMetric() {
        return CoreMetrics.OVERALL_CONDITIONS_TO_COVER;
    }

    @Override
    public Metric getCoverageMetric() {
        return CoreMetrics.OVERALL_BRANCH_COVERAGE;
    }

    @Override
    public Metric getToCoverByLineMetric() {
        return CoreMetrics.OVERALL_CONDITIONS_BY_LINE;
    }

    @Override
    public Metric getCoveredByLineMetric() {
        return CoreMetrics.OVERALL_COVERED_CONDITIONS_BY_LINE;
    }


}
