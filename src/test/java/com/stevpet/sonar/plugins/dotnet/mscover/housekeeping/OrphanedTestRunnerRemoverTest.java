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
package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyString;

public class OrphanedTestRunnerRemoverTest {

    private OrphanedTestRunnerRemover orphanedTestRunnerRemover;
    private @Mock MwicBridge processHelper;

    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        orphanedTestRunnerRemover = new OrphanedTestRunnerRemover(processHelper);
    }
    
    @Test
    public void invocations() {
        orphanedTestRunnerRemover.execute() ;
        verify(processHelper,times(1)).getProcessInfoFromName("vstest.executionengine.exe");
        verify(processHelper,times(1)).getProcessInfoFromName("vstest.console.exe");
        verify(processHelper,times(1)).getProcessInfoFromName("opencover.console.exe");
        verify(processHelper,times(0)).killProcessId(anyString());
    }
    @Test
    public void noOrphanedTestRunnerLeave() {

       List<ProcessInfo> engineValues = new ArrayList<>();
       engineValues.add(new ProcessInfo("vstest.executionengine.exe","100","200"));
       
       List<ProcessInfo> consoleValues=new ArrayList<>();
       consoleValues.add(new ProcessInfo("vstest.console.exe","200","300"));
       
       List<ProcessInfo> opencoverValues=new ArrayList<>();
       opencoverValues.add(new ProcessInfo("opencover.console.exe","300","400"));
       
       when(processHelper.getProcessInfoFromName("vstest.executionengine.exe")).thenReturn(engineValues);
       when(processHelper.getProcessInfoFromName("vstest.console.exe")).thenReturn(consoleValues);
       when(processHelper.getProcessInfoFromName("opencover.console.exe")).thenReturn(opencoverValues);
       
       orphanedTestRunnerRemover.execute();
       
       verify(processHelper,times(0)).killProcessId(anyString());
      
    }
    
    @Test
    public void oneOrphanedTestRunnerLeave() {

       List<ProcessInfo> engineValues = new ArrayList<>();
       engineValues.add(new ProcessInfo("vstest.executionengine.exe","100","200"));
       engineValues.add(new ProcessInfo("vstest.executionengine.exe","101","201"));
       
       List<ProcessInfo> consoleValues=new ArrayList<>();
       consoleValues.add(new ProcessInfo("vstest.console.exe","200","300"));
       consoleValues.add(new ProcessInfo("vstest.console.exe","201","301"));     
       List<ProcessInfo> opencoverValues=new ArrayList<>();
       opencoverValues.add(new ProcessInfo("opencover.console.exe","300","400"));
       
       when(processHelper.getProcessInfoFromName("vstest.executionengine.exe")).thenReturn(engineValues);
       when(processHelper.getProcessInfoFromName("vstest.console.exe")).thenReturn(consoleValues);
       when(processHelper.getProcessInfoFromName("opencover.console.exe")).thenReturn(opencoverValues);
       
       orphanedTestRunnerRemover.execute();
       
       verify(processHelper,times(1)).killProcessId("101");
      
    }
    
    @Test
    public void noOrphanedTestRunnerLeave2() {

       List<ProcessInfo> engineValues = new ArrayList<>();
       engineValues.add(new ProcessInfo("vstest.executionengine.exe","100","200"));
       engineValues.add(new ProcessInfo("vstest.executionengine.exe","101","201"));
       
       List<ProcessInfo> consoleValues=new ArrayList<>();
       consoleValues.add(new ProcessInfo("vstest.console.exe","200","300"));
       consoleValues.add(new ProcessInfo("vstest.console.exe","201","301"));     
       List<ProcessInfo> opencoverValues=new ArrayList<>();
       opencoverValues.add(new ProcessInfo("opencover.console.exe","300","400"));
       opencoverValues.add(new ProcessInfo("opencover.console.exe","301","400"));
       
       when(processHelper.getProcessInfoFromName("vstest.executionengine.exe")).thenReturn(engineValues);
       when(processHelper.getProcessInfoFromName("vstest.console.exe")).thenReturn(consoleValues);
       when(processHelper.getProcessInfoFromName("opencover.console.exe")).thenReturn(opencoverValues);
       
       orphanedTestRunnerRemover.execute();
       
       verify(processHelper,times(0)).killProcessId(anyString());
      
    }
    
    
    @Test
    public void twoOrphanedTestRunners() {

       List<ProcessInfo> engineValues = new ArrayList<>();
       engineValues.add(new ProcessInfo("vstest.executionengine.exe","100","200"));
       engineValues.add(new ProcessInfo("vstest.executionengine.exe","101","201"));
       
       List<ProcessInfo> consoleValues=new ArrayList<>();
       consoleValues.add(new ProcessInfo("vstest.console.exe","200","300"));
       consoleValues.add(new ProcessInfo("vstest.console.exe","201","301"));     
       List<ProcessInfo> opencoverValues=new ArrayList<>();
       
       when(processHelper.getProcessInfoFromName("vstest.executionengine.exe")).thenReturn(engineValues);
       when(processHelper.getProcessInfoFromName("vstest.console.exe")).thenReturn(consoleValues);
       when(processHelper.getProcessInfoFromName("opencover.console.exe")).thenReturn(opencoverValues);
       
       orphanedTestRunnerRemover.execute();
       
       verify(processHelper,times(1)).killProcessId("100");
       verify(processHelper,times(1)).killProcessId("101");
      
    }
    @Test
    public void orphanedTestRunnerLeave() {
       List<ProcessInfo> engineValues = new ArrayList<>();
       engineValues.add(new ProcessInfo("vstest.executionengine.exe","100","200"));
       
       List<ProcessInfo> consoleValues=new ArrayList<>();
       consoleValues.add(new ProcessInfo("vstest.console.exe","200","300"));
       
       List<ProcessInfo> openCoverValues=new ArrayList<>();
       
       when(processHelper.getProcessInfoFromName("vstest.executionengine.exe")).thenReturn(engineValues);
       when(processHelper.getProcessInfoFromName("vstest.console.exe")).thenReturn(consoleValues);
       when(processHelper.getProcessInfoFromName("opencover.console.exe")).thenReturn(openCoverValues);
       
       orphanedTestRunnerRemover.execute();
       
       verify(processHelper,times(1)).killProcessId("100");
      
    }
}

