package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import java.util.ArrayList;
import java.util.List;

public class SonarLinePoints implements CoverageLinePoints {
    List<CoverageLinePoint> points = new ArrayList<CoverageLinePoint>();

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoints#getLast()
     */
    public SonarLinePoint getLast() {
        int last=size()-1;
        return (SonarLinePoint)points.get(last);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoints#size()
     */
    public int size() {
        return points.size();
    }

    public SonarLinePoint addPoint(int line, boolean b) {
        SonarLinePoint point;
        if(size()==0 || getLastLine() != line) {
            point = new SonarLinePoint();
            point.setLine(line);
            point.setCovered(b);
            points.add(point);
        } 
        point = getLast();
        if(!b) {
            point.setCovered(false);
        }
        return point;
    }

    private int getLastLine() {
        return getLast().getLine();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoints#getSummary()
     */
    public SonarCoverageSummary getSummary() {
        SonarCoverageSummary summary = new SonarCoverageSummary();
        for(CoveragePoint point: points) {
            summary.incrementPoint(point);

        }
        return summary;
    }

    public List<CoverageLinePoint> getPoints() {
        return points;
    }
    
    
}
