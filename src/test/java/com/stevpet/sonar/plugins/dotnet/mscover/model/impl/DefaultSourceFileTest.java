package com.stevpet.sonar.plugins.dotnet.mscover.model.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFile;

public class DefaultSourceFileTest {

    private SourceFile sourceFile ;
    
    @Test
    public void testSourceFile() {
        sourceFile = new DefaultSourceFile("1","aap");
        assertEquals("1",sourceFile.getId());
        assertEquals("aap",sourceFile.getPath());
    }
}
