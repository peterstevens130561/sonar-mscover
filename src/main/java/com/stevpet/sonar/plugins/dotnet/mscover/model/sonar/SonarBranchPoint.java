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

public class SonarBranchPoint implements CoverageLinePoint{
    private int line;
    private int branchesVisited ;
    private int branchesToVisit ;
    public SonarBranchPoint(int line, int visited, int toVisit) {
        this.line=line;
        this.branchesVisited=visited;
        this.branchesToVisit=toVisit;
    }
    /**
     * @return the line
     */
    public int getLine() {
        return line;
    }
    /**
     * @param line the line to set
     */
    public void setLine(int line) {
        this.line = line;
    }
    /**
     * @return the branchesVisited
     */
    public int getCovered() {
        return branchesVisited;
    }

    /**
     * @return the branchesToVisit
     */
    public int getToCover() {
        return branchesToVisit;
    }

    void incrementBranchesToVisit() {
        this.branchesToVisit +=1;
    }
    void incrementVisitedBranches() {
        this.branchesVisited +=1;
    }

	
	@Override
	public boolean equals(Object o) {
	    if(o==null) {
	        return false ;
	    }
	    SonarBranchPoint other = (SonarBranchPoint) o;
	    return this.branchesToVisit == other.branchesToVisit  &&
	            this.branchesVisited==other.branchesVisited &&
	            this.line==other.line;
	}
}
