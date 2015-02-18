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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.model;

public class OpenCoverSequencePoint implements SequencePoint {
    private int line ;
    private int offset ;
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#getVisitedCount()
     */
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#getLine()
     */
    public int getLine() {
        return line;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#getOffset()
     */
    public int getOffset() {
        return offset;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#setOffset(java.lang.String)
     */
    public void setOffset(String string) {
        this.offset = Integer.parseInt(string);
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.model.opencover.SequencePoint#setStartLine(java.lang.String)
     */
    public void setStartLine(String string) {
       this.line = Integer.parseInt(string);
        
    }



    
}
