package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import static org.junit.Assert.*;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameRow;

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
        table.add(3,model);
        int index=table.getSourceFileId(name);
        assertEquals("expect index 4 for the item added",4,index);
    }
}
