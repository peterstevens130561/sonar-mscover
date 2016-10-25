/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
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
