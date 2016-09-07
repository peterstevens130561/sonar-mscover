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
package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import static org.junit.Assert.*;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;

public class SourceFileNameTable_GetSourceFileId_Test {
    private SourceFileNameTable table = new SourceFileNameTable();
    
    @Test
    public void firstToAdd_ExpectIndex1() {
        String name = "myname";
        int index=table.getSourceFileId(name);
        assertEquals("expect index 1 for the first to add",1,index);
    }
    
    @Test
    public void firstToAdd_ExpectFoundAt1() {
        String name = "myname";
        table.getSourceFileId(name);
        int index=table.getSourceFileId(name);
        assertEquals("expect index 1 for the first to add",1,index);
    }
    
    
    @Test
    public void secondToAdd_ExpectFoundAtNext() {
        String name = "myname";
        SourceFileNameRow model = new SourceFileNameRow(3,"strange");
        table.add(model);
        int index=table.getSourceFileId(name);
        assertEquals("expect index 4 for the item added",4,index);
    }
}
