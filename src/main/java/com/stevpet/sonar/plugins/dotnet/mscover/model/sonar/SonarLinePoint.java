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

public class SonarLinePoint implements CoverageLinePoint{
    private int line ;
    private int covered;
    
    
    public int getLine() {
        return line;
    }
    public void setLine(int line) {
        this.line = line;
    }
    public boolean isVisited() {
        return covered>0;
    }
    public void setCovered(boolean visited) {
        covered= visited?1:0;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint#getToCover()
     */
    public int getToCover() {
        return 1;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.CoveragePoint#getCovered()
     */
    public int getCovered() {
        return covered;
    }
    
   
}
