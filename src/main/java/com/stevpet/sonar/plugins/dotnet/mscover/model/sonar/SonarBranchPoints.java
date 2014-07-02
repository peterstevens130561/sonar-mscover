package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.SonarException;

public class SonarBranchPoints implements CoveragePoints {
    private List<SonarBranchPoint> points = new ArrayList<SonarBranchPoint>();

    public int size() {
        return points.size();
    }

    /**
     * @return last point in the list, if none an exception is thrown.
     */
    public SonarBranchPoint getLast() {
        int last=size()-1;
        return points.get(last);
    }

    
    /**
     * aggregate a branchpoint.
     * @param line of branchpoint
     * @param visited true if this branchpoint is visited
     */
    public SonarBranchPoint addPoint(int line, boolean visited) {
        SonarBranchPoint branchPoint;
        if(size() == 0) {
            branchPoint = addNewPoint(line);
        } else {
            branchPoint=getLast();
        }
        
        if(branchPoint.getLine() != line) {
            branchPoint = addNewPoint(line);
        }
        branchPoint.incrementBranchesToVisit();
        if(visited) {
            branchPoint.incrementVisitedBranches();
        }
        return branchPoint; 
    }

    private SonarBranchPoint addNewPoint(int line) {
        SonarBranchPoint branchPoint;
        branchPoint = new SonarBranchPoint();
        points.add(branchPoint);
        branchPoint.setLine(line);
        return branchPoint;
    }


    public SonarCoverageSummary getSummary() {
        SonarCoverageSummary summary = new SonarCoverageSummary();
        for(SonarBranchPoint point: points) {
            summary.incrementPoint(point);
        }
        return summary;
    }
    
}
