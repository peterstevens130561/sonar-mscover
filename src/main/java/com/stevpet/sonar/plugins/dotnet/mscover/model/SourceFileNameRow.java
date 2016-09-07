/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.model;


public class SourceFileNameRow {
    
    private int sourceFileID;
    private String sourceFileName;
    
    public SourceFileNameRow() {
    }
    

    public SourceFileNameRow(int id,String name) {
        sourceFileID=id;
        sourceFileName=name;
    }


    public int getSourceFileID() {
        return sourceFileID;
    }
    
    public String getSourceFileName() {
        return sourceFileName;
    }
    
    public SourceFileNameRow setSourceFileID(int value) {
        sourceFileID = value;
        return this;
    }

    public SourceFileNameRow setSourceFileName(String value) {
        sourceFileName = value;
        return this;
    }
}
