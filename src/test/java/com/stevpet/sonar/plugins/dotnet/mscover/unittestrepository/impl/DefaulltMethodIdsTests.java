package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.model.impl.DefaultMethodIds;

public class DefaulltMethodIdsTests {
    private MethodIds methods = new DefaultMethodIds();
    
    @Test
    public void emptyList() {
        long size = methods.stream().count();
        assertEquals("no elements",0,size);
    }
    
    @Test
    public void listWithOne() {
        MethodId methodA = new MethodId("a.dll","b","c","1");
        methods.add (methodA);
        long size = methods.stream().count();
        assertEquals("one elements",1,size);
        assertTrue(methods.stream().filter(m -> m.equals(methodA)).findFirst().isPresent());
    }
    
    @Test
    public void listWithTwo() {
        MethodId methodA = new MethodId("a.dll","b","c","1");
        methods.add (methodA);
        
        MethodId methodB = new MethodId("a.dll","b","c","2");
        methods.add (methodB);
        
        long size = methods.stream().count();
        assertEquals("one elements",2,size);
        assertTrue(methods.stream().filter(m -> m.equals(methodA)).findFirst().isPresent());
        assertTrue(methods.stream().filter(m -> m.equals(methodB)).findFirst().isPresent());
    }
}
