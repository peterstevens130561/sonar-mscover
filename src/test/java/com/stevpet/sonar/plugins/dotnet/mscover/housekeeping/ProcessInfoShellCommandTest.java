package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;

public class ProcessInfoShellCommandTest {
    private String normalResponse = "Name                        ParentProcessId  ProcessId\r\n" +
        "vstest.executionengine.exe  2220             10964 \r\n" + 
        "vstest.executionengine.exe  13680            5764  \r\n" +
        "vstest.executionengine.exe  15696            15908 \r\n";
    @Mock private CommandLineExecutor commandLineExecutor;

    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }
    @Test
    public void normalResponse() {
        ProcessHelper ph = new ProcessHelper(commandLineExecutor);
        when(commandLineExecutor.getStdOut()).thenReturn(normalResponse);
        List<ProcessInfo> pi=ph.getProcessInfoFromName("vstest.executionengine.exe");
        assertEquals(3,pi.size());
        assertEquals("vstest.executionengine.exe",pi.get(0).getName());
        assertEquals("2220",pi.get(0).getParentId());
        assertEquals("10964",pi.get(0).getId());
    }
    
    @Test
    public void noResponse() {
        ProcessHelper ph = new ProcessHelper(commandLineExecutor);
        when(commandLineExecutor.getStdOut()).thenReturn(null);
        List<ProcessInfo> pi=ph.getProcessInfoFromName("vstest.executionengine.exe");
        assertEquals(0,pi.size());
    }
    

}
