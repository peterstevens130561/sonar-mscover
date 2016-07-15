package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class ProcessKillShellCommandTest {
    private ProcessKillShellCommand killCommand ;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void validId() {
        String id="30";
        killCommand = new ProcessKillShellCommand(id);
        String commandLine=killCommand.toCommandLine();
        assertEquals("WMIC process where processid=\"30\" call terminate",commandLine);
    }
    
    @Test
    public void invalidId() {
        String id=null;
        try {
            new ProcessKillShellCommand(id);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("expected IllegalArgumentException on instantiation");
    }
}
