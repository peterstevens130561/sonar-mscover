package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SonarCoverageSummary implements CoveragePoint {
    private Logger LOG = LoggerFactory.getLogger(SonarCoverageSummary.class);
    private int toCover;
    private int covered;

    /**
     * @return the toCover
     */
    public int getToCover() {
        return toCover;
    }

    /**
     * @return the covered number of lines
     */
    public int getCovered() {
        return covered;
    }

    public void incrementPoint(CoveragePoint point) {
        toCover += point.getToCover();
        covered += point.getCovered();
    }

    /**
     * 
     * @return the coverage ratio ( 1 = 100%)
     */
    public double getCoverage() {
        if (getToCover() == 0) {
            return 1.;
        }
        double coverage = Math
                .round(((double) getCovered() / (double) getToCover()) * 100) * 0.01;
        if (coverage < 0) {
            LOG.error("negative coverage on " + this.toString()
                    + " must be programming error");
            coverage = 0;
        }
        return coverage;
    }
}


