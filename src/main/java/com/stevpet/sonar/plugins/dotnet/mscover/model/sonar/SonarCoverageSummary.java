package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

public class SonarCoverageSummary implements CoveragePoint {
    
    private int toCover ;
    private int covered ;
    
    /**
     * @return the toCover
     */
    public int getToCover() {
        return toCover;
    }

    /**
     * @return the covered
     */
    public int getCovered() {
        return covered;
    }

    public void incrementPoint(CoveragePoint point) {
        toCover += point.getToCover();
        covered += point.getCovered();
    }

}
