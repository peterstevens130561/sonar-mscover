package com.stevpet.sonar.plugins.dotnet.mscover.coveragesaver.defaultsaver;

import org.sonar.api.measures.Metric;

public interface LineCoverageMetrics {

    /**
     * Lines to cover
     * @return
     */
    Metric<?> getLinesToCoverMetric() ;
    /**
     * Lines not covered
     * @return
     */
    Metric<?> getUncoveredLinesMetric();
    /**
     * Percentage covered
     * @return
     */
    Metric<?> getCoverageMetric() ;
    /**
     * Percentage of lines covered
     * @return
     */
    Metric<Double> getLineCoverageMetric();
    /**
     * Coverage per line
     * @return
     */
    Metric<String> getCoverageLineHitsDataMetric();
}
