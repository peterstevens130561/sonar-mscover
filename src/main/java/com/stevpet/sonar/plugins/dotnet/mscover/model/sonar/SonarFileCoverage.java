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


/**
 * Coverage info per file
 */
public class SonarFileCoverage {
    String absolutePath ;

    private CoverageLinePoints linePoints = new SonarLinePoints();
    private CoverageLinePoints branchPoints = new SonarBranchPoints();
    
    public void setAbsolutePath(String absolutePath) {  
        this.absolutePath=absolutePath;
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

    public CoveragePoint addLinePoint(int line, boolean covered) {
        return getLinePoints().addPoint(line,covered);
    }

    public CoveragePoint getLastLinePoint() {
        return getLinePoints().getLast();
    }

    public void addBranchPoint(int line, boolean covered) {
        getBranchPoints().addPoint(line, covered);
    }

}
