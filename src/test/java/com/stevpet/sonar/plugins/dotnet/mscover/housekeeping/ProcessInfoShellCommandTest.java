/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
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
        MwicBridge ph = new MwicBridge(commandLineExecutor);
        when(commandLineExecutor.getStdOut()).thenReturn(normalResponse);
        List<ProcessInfo> pi=ph.getProcessInfoFromName("vstest.executionengine.exe");
        assertEquals(3,pi.size());
        assertEquals("vstest.executionengine.exe",pi.get(0).getName());
        assertEquals("2220",pi.get(0).getParentId());
        assertEquals("10964",pi.get(0).getId());
    }
    
    @Test
    public void noResponse() {
        MwicBridge ph = new MwicBridge(commandLineExecutor);
        when(commandLineExecutor.getStdOut()).thenReturn(null);
        List<ProcessInfo> pi=ph.getProcessInfoFromName("vstest.executionengine.exe");
        assertEquals(0,pi.size());
    }
    

}
