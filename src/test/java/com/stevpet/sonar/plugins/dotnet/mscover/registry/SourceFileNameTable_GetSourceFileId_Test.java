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
package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import static org.junit.Assert.*;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.FileId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFile;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class SourceFileNameTable_GetSourceFileId_Test {
    private SourceFileNameTable table = new SourceFileNameTable();
    
    @Test
    public void firstToAdd_ExpectIndex1() {
        SourceFile name = new SourceFile("myname");
        FileId fileId=table.getSourceFileId(name);
        assertEquals("expect index 1 for the first to add",1,(int)fileId.getId());
    }
    
    @Test
    public void firstToAdd_ExpectFoundAt1() {
        SourceFile name = new SourceFile("myname");
        table.getSourceFileId(name);
        FileId fileId=table.getSourceFileId(name);
        assertEquals("expect index 1 for the first to add",1,(int)fileId.getId());
    }
    
    
    @Test
    public void secondToAdd_ExpectFoundAtNext() {
        SourceFile name = new SourceFile("myname");
        SourceFileNameRow model = new SourceFileNameRow(new FileId("3"),new SourceFile("strange"));
        table.add(model);
        FileId fileId=table.getSourceFileId(name);
        assertEquals("expect index 4 for the item added",4,(int)fileId.getId());
    }
}
