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

/**
 * Coverage info per file
 */
public class SonarFileCoverage  {
    /**
     * 
     */

    private String absolutePath;

    private SonarLinePoints linePoints = new SonarLinePoints();
    private SonarBranchPoints branchPoints = new SonarBranchPoints();

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public CoverageLinePoints getLinePoints() {
        return linePoints;
    }

    public CoverageLinePoints getBranchPoints() {
        return branchPoints;
    }

    public CoverageLinePoint addLinePoint(int line, boolean covered) {
        return getLinePoints().addPoint(line, covered);
    }

    public CoverageLinePoint getLastLinePoint() {
        return getLinePoints().getLast();
    }

    public void addBranchPoint(int line, boolean covered) {
        getBranchPoints().addPoint(line, covered);
    }
    
    void merge(SonarFileCoverage sourceFileCoverage) {
        CoverageLinePoints sourceCoverageLinePoints = sourceFileCoverage.getLinePoints(); 
        getLinePoints().merge(sourceCoverageLinePoints);

        sourceCoverageLinePoints = sourceFileCoverage.getBranchPoints(); 
        getBranchPoints().merge(sourceCoverageLinePoints);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        }
        SonarFileCoverage other = (SonarFileCoverage) otherObject;
        return  ((this.absolutePath == null && other.absolutePath == null) || 
                (this.absolutePath !=null && this.absolutePath.equals(other.absolutePath))) &&
                this.branchPoints.equals(other.branchPoints) &&
                this.linePoints.equals(other.linePoints);
    }

    public void addBranchPoint(int line, int branchPath, boolean visited) {
        getBranchPoints().addPoint(line, visited);
    }
}
