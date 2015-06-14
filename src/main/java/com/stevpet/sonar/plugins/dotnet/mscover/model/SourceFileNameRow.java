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
    
    private FileId sourceFileID;
    private SourceFile sourceFile;
    
    public SourceFileNameRow() {
    }
    

    public SourceFileNameRow(FileId fileId,SourceFile sourceFile) {
        sourceFileID=fileId;
        this.sourceFile=sourceFile;
    }


    public FileId getSourceFileID() {
        return sourceFileID;
    }
    
    public SourceFile getSourceFile() {
        return sourceFile;
    }
    
    public SourceFileNameRow setSourceFileID(String id) {
        sourceFileID = new FileId(id);
        return this;
    }

    /**
     * Set absolutePath to source file
     * @param absolutePath
     * @return
     */
    public SourceFileNameRow setSourceFileName(String absolutePath) {
        this.sourceFile = new SourceFile(absolutePath);
        return this;
    }
}
