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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SonarLinePoints implements CoverageLinePoints,Serializable {
    private List<CoverageLinePoint> points = new ArrayList<CoverageLinePoint>();

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoints#getLast()
     */
    public SonarLinePoint getLast() {
        int last=size()-1;
        return (SonarLinePoint)points.get(last);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoints#size()
     */
    public int size() {
        return points.size();
    }

    /**
     * add a point. If the line is the same as the previous one added, and this one is not covered, then the line is considered not covered
     */
    public SonarLinePoint addPoint(int line, boolean b) {
        SonarLinePoint point;
        if(size()==0 || getLastLine() != line) {
            point = new SonarLinePoint();
            point.setLine(line);
            point.setCovered(b);
            points.add(point);
        } 
        point = getLast();
        if(!b) {
            point.setCovered(false);
        }
        return point;
    }

    private int getLastLine() {
        return getLast().getLine();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoints#getSummary()
     */
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
    
    @Override
    public boolean equals(Object o) {
        if(o==null) {
            return false ;
        }
        SonarLinePoints other = (SonarLinePoints) o;
        return this.points.equals(other.points);
    }
    
}
