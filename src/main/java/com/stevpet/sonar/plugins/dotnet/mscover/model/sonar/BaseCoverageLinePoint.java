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

public abstract class BaseCoverageLinePoint  implements CoverageLinePoint {
   protected int line;
   protected int covered;
   protected int toCover;
   

    public int getLine() {
        return line;
    }
    /**
     * @param line the line to set
     */
    public void setLine(int line) {
        this.line = line;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint#getCovered()
     */
    @Override
    public int getCovered() {
        return covered;
    }
    
    @Override
    public void setCovered(int covered) {
        this.covered = covered;
    }
    
    public void setCovered(boolean visited) {
        covered= visited?1:0;
    }
    
    public int getToCover() {
        return toCover;
    }
    
    public boolean equals(Object o) {
        if(o==null) {
            return false;
        }
        BaseCoverageLinePoint other = (BaseCoverageLinePoint) o;
        return this.covered == other.covered && 
                this.line == other.line &&
                this.toCover == other.toCover;
    }
    
    /**
     * merge the coverage information of the other one with this one, updating this.
     * @param other a coveragelinepoint on the same line
     */
}
