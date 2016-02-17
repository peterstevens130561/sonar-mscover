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

import com.google.common.base.Preconditions;

public class SonarLinePoint extends BaseCoverageLinePoint<SonarLinePoint>  {

    public SonarLinePoint() {
        toCover=1;
    }
    public SonarLinePoint(int line, boolean covered) {
        this();
        this.line=line;
        setCovered(covered); 
    }
    

    @Override
    public void merge(SonarLinePoint other) {
        Preconditions.checkArgument(other.line == this.line,"line differ: other.line=" + other.line + " this.line" + this.line);
        this.covered += other.covered;
        
    }
}
