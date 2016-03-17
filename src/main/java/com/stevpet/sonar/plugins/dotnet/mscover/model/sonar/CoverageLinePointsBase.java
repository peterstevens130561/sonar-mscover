package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;

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
    public CoverageLinePoint getLast() {
        int last = size() - 1;
        if (last < 0) {
            return null;
        }
        return (CoverageLinePoint) points.get(last);
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
    public void merge(CoverageLinePoints sourceLinePoints) {
        if (size() == 0) {
            mergeIntoEmptyDestination(sourceLinePoints.getPoints());
            return;
        }
        mergeIntoDestinationWithDifferentSize(sourceLinePoints.getPoints());

    }

    private void mergeIntoDestinationWithDifferentSize(List<CoverageLinePoint> sourceLinePoints) {
        List<CoverageLinePoint> mergedList = new ArrayList<CoverageLinePoint>();
        int sourceIndex = 0, destinationIndex = 0;
        int sourceSize = sourceLinePoints.size();
        int destinationSize = points.size();
        while (sourceIndex < sourceSize && destinationIndex < destinationSize)
        {
            CoverageLinePoint sourceLinePoint = sourceLinePoints.get(sourceIndex);
            CoverageLinePoint destinationLinePoint = points.get(destinationIndex);
            int sourceLine = sourceLinePoint.getLine();
            int destinationLine = destinationLinePoint.getLine();
            
            if (destinationLine < sourceLine) {
                mergedList.add(destinationLinePoint);
                destinationIndex++;
           } else if (destinationLine > sourceLine) {
                mergedList.add(sourceLinePoint);
                sourceIndex++;
            } else  {
                destinationLinePoint.merge(sourceLinePoint);
                mergedList.add(destinationLinePoint);
                ++destinationIndex;
                ++sourceIndex;
            } 
        }
        while (sourceIndex < sourceSize) {
            mergedList.add(sourceLinePoints.get(sourceIndex));
            ++sourceIndex;
        }
        while (destinationIndex < destinationSize) {
            mergedList.add(points.get(destinationIndex));
            ++destinationIndex;
        }
        points = mergedList;
    }


    private void mergeIntoEmptyDestination(
            List<CoverageLinePoint> sourceLinePoints) {
        points.addAll(sourceLinePoints);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        CoverageLinePointsBase other = (CoverageLinePointsBase) o;
        return points.equals(other.points);
    }

}
