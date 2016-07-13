package com.stevpet.sonar.plugins.dotnet.mscover.controltest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

public class TestExecutorMonitorTest {

    TestExecutorMonitor testStateMonitor = new DefaultTestStateMonitor() ;
    @Mock private TestRunner testRunner ;
    private ENDSTATE result; 
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }
    @Test
    public void simpleFlow() throws Exception {
        // pass the execution object to the monitor
        testStateMonitor.setTestRunner(testRunner);
        // start the monitor
        result=testStateMonitor.call();
        // the monitor triggers the executor in its own thread
        // it listens to the output
        //verify(testRunner,times(1)).execute();
        // the execution runs ok
        // the execution completes
        // the monitor completes and returns OK
        assertEquals(ENDSTATE.COMPLETED,result);
           
    }
    
}
