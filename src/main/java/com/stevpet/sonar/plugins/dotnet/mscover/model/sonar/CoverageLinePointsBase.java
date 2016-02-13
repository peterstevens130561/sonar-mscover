package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import java.util.ArrayList;
import java.util.List;

public abstract class CoverageLinePointsBase implements CoverageLinePoints {
    protected List<CoverageLinePoint> points = new ArrayList<CoverageLinePoint>();
    
    @Override
    public int size() {
        return points.size();
    }
    
    /**
     * @return 
     * @return last point in the list, if none an exception is thrown.
     */
    @Override
    public  CoverageLinePoint getLast() {
        int last=size()-1;
        return points.get(last);
    }
    
    @Override
    public List<CoverageLinePoint> getPoints() {
        return points;
    }

    @Override
    public SonarCoverageSummary getSummary() {
        SonarCoverageSummary summary = new SonarCoverageSummary();
        for(CoverageLinePoint point: points) {
            summary.incrementPoint(point);
        }
        return summary;
    }
 /**   
    @Override
    public void merge(CoverageLinePoints sourceLinePoints) {
        if(size()==0) {
            mergeIntoEmptyDestination(sourceLinePoints.getPoints());
            return;
        }
        if(size() != sourceLinePoints.size()) {
            throw new SonarCoverageException("Merging failed due to difference in number of linepoints ");
        }
        mergeIntoDestinationWithSamesSize(sourceLinePoints.getPoints());
    }
    
    **/
    private void mergeIntoDestinationWithSamesSize(
            List<CoverageLinePoint> sourceLinePoints) {
        for(int index=0;index< sourceLinePoints.size();index++) {
            CoverageLinePoint linePoint=sourceLinePoints.get(index);
            int covered = linePoint.getCovered();
            if(covered>0) {
                points.set(index,linePoint);
            }
        }
    }

    private void mergeIntoEmptyDestination(
            List<CoverageLinePoint> sourceLinePoints) {
        for(int index=0;index<sourceLinePoints.size();index++) {
            CoverageLinePoint linePoint=sourceLinePoints.get(index);
            int line=linePoint.getLine();
            int covered = linePoint.getCovered();
            addPoint(line, covered>0);
        }
    }
}
