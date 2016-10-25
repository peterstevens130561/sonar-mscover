/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.controltest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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
        //assertEquals(ENDSTATE.COMPLETED,result);
           
    }
    
}
