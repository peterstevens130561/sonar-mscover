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
