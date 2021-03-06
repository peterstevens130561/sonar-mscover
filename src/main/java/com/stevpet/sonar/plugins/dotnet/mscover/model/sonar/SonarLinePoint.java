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

import com.google.common.base.Preconditions;

public class SonarLinePoint extends BaseCoverageLinePoint{

    public SonarLinePoint() {
        toCover=1;
    }
    public SonarLinePoint(int line, boolean covered) {
        this();
        this.line=line;
        setCovered(covered); 
    }
    

    @Override 
    public int getCovered() {
        return covered>0?1:0;
    }
    
    @Override
    public void merge(CoveragePoint source) {
        SonarLinePoint other = (SonarLinePoint)source;
        Preconditions.checkArgument(other.line == this.line,"line differ: other.line=" + other.line + " this.line" + this.line);
        if(other.covered > 0 ) {
            this.covered=1;
        }
        
    }
}
