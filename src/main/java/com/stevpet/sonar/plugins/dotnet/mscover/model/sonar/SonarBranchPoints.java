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


public class SonarBranchPoints extends CoverageLinePointsBase {

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
            branchPoint=(SonarBranchPoint) getLast();
        }
        
        if(branchPoint.getLine() != line) {
            branchPoint = addNewPoint(line);
        } 
        branchPoint.addPath(visited);

        return branchPoint; 
    }

    private SonarBranchPoint addNewPoint(int line) {
        SonarBranchPoint branchPoint;
        branchPoint = new SonarBranchPoint(line);
        points.add(branchPoint);
        return branchPoint;
    }

    
}
