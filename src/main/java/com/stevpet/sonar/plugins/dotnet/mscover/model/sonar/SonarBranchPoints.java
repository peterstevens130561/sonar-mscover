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

import java.util.ArrayList;
import java.util.List;

public class SonarBranchPoints implements CoverageLinePoints {
    private List<CoverageLinePoint> points = new ArrayList<CoverageLinePoint>();

    public int size() {
        return points.size();
    }

    /**
     * @return last point in the list, if none an exception is thrown.
     */
    public SonarBranchPoint getLast() {
        int last=size()-1;
        return (SonarBranchPoint)points.get(last);
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
        for(CoveragePoint point: points) {
            summary.incrementPoint(point);
        }
        return summary;
    }

    public List<CoverageLinePoint> getPoints() {
        return points;
    }
    
}
