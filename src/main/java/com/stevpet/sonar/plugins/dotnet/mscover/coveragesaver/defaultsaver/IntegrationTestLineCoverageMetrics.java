package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;

public class IntegrationTestLineCoverageMetrics implements LineCoverageMetrics {

    @Override
    public Metric<?> getLinesToCoverMetric() {
        return CoreMetrics.IT_LINES_TO_COVER;
    }

    @Override
    public Metric<?> getUncoveredLinesMetric() {
        return CoreMetrics.IT_UNCOVERED_LINES;
    }

    @Override
    public Metric<?> getCoverageMetric() {
        return CoreMetrics.IT_COVERAGE;
    }

    @Override
    public Metric<?> GetLineCoverageMetric() {
        return CoreMetrics.IT_LINE_COVERAGE;
    }

    @Override
    public Metric<?> getCoverageLineHitsDataMetric() {
        return CoreMetrics.IT_COVERAGE_LINE_HITS_DATA;
    }

}
