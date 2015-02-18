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
package com.stevpet.sonar.plugins.dotnet.mscover.model;

public class SourceFileNameRow {
    
    private static final String SOURCE_FILE_NAME_ELEMENTNAME = "SourceFileName";
    private static final String SOURCE_FILE_ID_ELEMENTNAME = "SourceFileID";
    private int sourceFileID;
    private String sourceFileName;
    
    public  void setField(String name, String text) {
        if(name.equals(SOURCE_FILE_ID_ELEMENTNAME)) {
            sourceFileID = Integer.parseInt(text);
        } else if(name.equals(SOURCE_FILE_NAME_ELEMENTNAME)) {
            sourceFileName = text;
        }
              
    }

    public SourceFileNameRow(int id,String name) {
        sourceFileID=id;
        sourceFileName=name;
    }
    public SourceFileNameRow() {
    }

    public int getSourceFileID() {
        return sourceFileID;
    }
    
    public String getSourceFileName() {
        return sourceFileName;
    }
    
    public void setSourceFileID(int value) {
        sourceFileID = value;
    }

    public void setSourceFileName(String value) {
        sourceFileName = value;
    }
}
