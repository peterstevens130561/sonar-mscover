package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.MethodRepository;

public class DefaultMethodRepositoryTests {
    private MethodRepository repository = new DefaultMethodRepository();
    
    @Test
    public void emptyRepositoryShouldGiveEmptyList() {
        List<MethodId> list = repository.getMethods("20");
        assertNotNull("should have valid list",list);
        assertEquals("list should have no elements",0,list.size());
    }
    
    @Test
    public void repositoryShouldReturnElements() {
        MethodId method = new MethodId("a.dll","b","c","1");
        repository.addMethod("1", method);
        List<MethodId> list= repository.getMethods("1");
        assertNotNull("should have valid list",list);
        assertEquals("list should have one element",1,list.size());
        assertEquals(method,list.get(0));
    }
    
    @Test
    public void repositoryShouldReturnTwoElements() {
        MethodId methodA = new MethodId("a.dll","b","c","1");
        repository.addMethod("1", methodA);
        MethodId methodB = new MethodId("a.dll","b","c","2");
        repository.addMethod("1", methodB);
        List<MethodId> list= repository.getMethods("1");
        assertNotNull("should have valid list",list);
        assertEquals("list should have two elements",2,list.size());
        assertEquals(methodA,list.get(0));
        assertEquals(methodB,list.get(1));
    }
    
    @Test
    public void repositoryShouldNotReturnElements() {
        MethodId method = new MethodId("a.dll","b","c","1");
        repository.addMethod("1", method);
        List<MethodId> list= repository.getMethods("2");
        assertNotNull("should have valid list",list);
        assertEquals("list should have no element",0,list.size());
    }
}
