package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodId;
import com.stevpet.sonar.plugins.dotnet.mscover.model.MethodIds;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultMethodRepository;

public class DefaultMethodRepositoryTests {
    private MethodRepository repository = new DefaultMethodRepository();
    
    @Test
    public void emptyRepositoryShouldGiveEmptyList() {
        MethodIds list = repository.getMethods("20");
        assertNotNull("should have valid list",list);
        assertEquals("list should have no elements",0,list.stream().count());
    }
    
    @Test
    public void repositoryShouldReturnElements() {
        MethodId method = new MethodId("a.dll","b","c","1");
        repository.addMethod("1", method);
        MethodIds list= repository.getMethods("1");
        assertNotNull("should have valid list",list);
        assertEquals("list should have one element",1,list.stream().count());
        assertTrue(list.stream().filter(m -> m.equals(method)).findFirst().isPresent());
    }
    
    @Test
    public void repositoryShouldReturnTwoElements() {
        MethodId methodA = new MethodId("a.dll","b","c","1");
        repository.addMethod("1", methodA);
        MethodId methodB = new MethodId("a.dll","b","c","2");
        repository.addMethod("1", methodB);
        MethodIds list= repository.getMethods("1");
        assertNotNull("should have valid list",list);
        assertEquals("list should have two elements",2,list.stream().count());
        assertTrue(list.stream().filter(m -> m.equals(methodA)).findFirst().isPresent());
        assertTrue(list.stream().filter(m -> m.equals(methodB)).findFirst().isPresent());
    }
    
    @Test
    public void repositoryShouldNotReturnElements() {
        MethodId method = new MethodId("a.dll","b","c","1");
        repository.addMethod("1", method);
        MethodIds list= repository.getMethods("2");
        assertNotNull("should have valid list",list);
        assertEquals("list should have no element",0,list.stream().count());
    }
}
