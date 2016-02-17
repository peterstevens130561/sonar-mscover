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

import com.google.common.base.Preconditions;

public class SonarBranchPoint extends BaseCoverageLinePoint<SonarBranchPoint> {
    private List<Boolean> paths = new ArrayList<>();

    public SonarBranchPoint(int line) {
        super();
        this.line=line;
    }

    /**
     * use to add next BracnPoint on same line
     * 
     * @param visited
     */
    public void addPath(boolean visited) {
        paths.add(paths.size(), visited);
        toCover += 1;
        if (visited) {
            ++covered;
        }
    }

    @Override
    public void merge(SonarBranchPoint other) {
        Preconditions.checkArgument(other.paths.size() == paths.size(), "different number of paths: other" + this.paths.size() + " this" + other.paths.size());
        Preconditions.checkArgument(other.line == this.line,"diffent line: other" + other.line + " this" + this.line );
        int items=paths.size();
        for(int index=0;index<items;++index) {
            if(!this.paths.get(index) && other.paths.get(index)) {
                covered++;
                this.paths.set(index, true);
            }
        }
    }





}
