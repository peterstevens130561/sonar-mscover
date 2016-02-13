/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.model.sonar;

import java.util.List;

public class SonarBranchPoints extends CoverageLinePointsBase {

    /**
     * @return last point in the list, if none an exception is thrown.
     */
    @Override
    public SonarBranchPoint getLast() {
        int last = size() - 1;
        return (SonarBranchPoint) points.get(last);
    }

    public SonarBranchPoint addPoint(int line, int visited) {
        int toVisit = 1;
        SonarBranchPoint branchPoint;
        if (size() == 0) {
            SonarBranchPoint newPoint = new SonarBranchPoint(line, visited,
                    toVisit);
            points.add(newPoint);
            return newPoint;
        }

        branchPoint = getLast();
        if (line != branchPoint.getLine()) {
            SonarBranchPoint newPoint = new SonarBranchPoint(line, visited,
                    toVisit);
            points.add(newPoint);
            return newPoint;
        }
        visited += branchPoint.getCovered();
        toVisit += branchPoint.getToCover();
        SonarBranchPoint newPoint = new SonarBranchPoint(line, visited, toVisit);
        points.set(points.size() - 1, newPoint);
        return newPoint;
    }

    public SonarCoverageSummary getSummary() {
        SonarCoverageSummary summary = new SonarCoverageSummary();
        for (CoveragePoint point : points) {
            // TODO: wtf
            summary.incrementPoint(point);
        }
        return summary;
    }

    public List<CoverageLinePoint> getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        SonarBranchPoints other = (SonarBranchPoints) o;
        return points.equals(other.points);
    }

}
