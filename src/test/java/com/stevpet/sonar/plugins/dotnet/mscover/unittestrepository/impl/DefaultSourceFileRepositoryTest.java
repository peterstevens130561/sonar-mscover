package com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.unittestrepository.SourceFileRepository;

public class DefaultSourceFileRepositoryTest {
    private SourceFileRepository repository = new DefaultSourceFileRepository();
    
    @Test
    public void empty() {
        String id=repository.getId("C:/fun");
        assertNull("should not be found",id);               
    }
    
    @Test
    public void nonExistent() {
        repository.addFile("1", "C:/bogus");
        String id=repository.getId("C:/fun");
        assertNull("should not be found",id);
    }
    
    @Test
    public void existing() {
        repository.addFile("1", "C:/bogus");
        String id=repository.getId("C:/bogus");
        assertEquals("should  be found","1",id);  
    }
}
