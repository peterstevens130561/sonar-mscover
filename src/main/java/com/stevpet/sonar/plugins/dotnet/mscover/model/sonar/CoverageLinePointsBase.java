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
        if(last < 0) {
            return null;
        }
        return (CoverageLinePoint)points.get(last);
    }
    
    @Override
    public List<CoverageLinePoint> getPoints() {
        return points;
    }

    @Override
    public SonarCoverageSummary getSummary() {
        SonarCoverageSummary summary = new SonarCoverageSummary();
        summary.addAll(points);
        return summary;
    }

    @Override
    public
    void merge(CoverageLinePoints sourceLinePoints) {
        if(size()==0) {
            mergeIntoEmptyDestination(sourceLinePoints.getPoints());
            return;
        }
        if(size() != sourceLinePoints.size()) {
            throw new SonarCoverageException("Merging failed due to difference in number of linepoints ");
        }
        mergeIntoDestinationWithSamesSize(sourceLinePoints.getPoints());
    }
    

    private void mergeIntoDestinationWithSamesSize(
            List<CoverageLinePoint> sourceLinePoints) {
        for(int index=0;index< sourceLinePoints.size();index++) {
            CoverageLinePoint sourceLinePoint=sourceLinePoints.get(index);
            CoverageLinePoint destinationLinePoint = points.get(index);
            destinationLinePoint.merge(sourceLinePoint);
        }
    }

    private void mergeIntoEmptyDestination(
            List<CoverageLinePoint> sourceLinePoints) {
        points.addAll(sourceLinePoints);
        /*
        for(int index=0;index<sourceLinePoints.size();index++) {
            CoverageLinePoint linePoint=sourceLinePoints.get(index);
            int line=linePoint.getLine();
            int covered = linePoint.getCovered();
            addPoint(line, covered>0);
        }
        */
    }
    
    @Override
    public boolean equals(Object o) {
        if(o==null) {
            return false ;
        }
        CoverageLinePointsBase other = (CoverageLinePointsBase)o;
        return points.equals(other.points);
    }

}
