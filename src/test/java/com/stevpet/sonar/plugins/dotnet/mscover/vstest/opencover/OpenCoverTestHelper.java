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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;

public class OpenCoverTestHelper {

    protected static String EXECUTABLE = "jippie";
    protected OpenCoverCommand openCoverCommand;

    public OpenCoverTestHelper() {
        openCoverCommand = new OpenCoverCommand();
        openCoverCommand.setInstallDir(EXECUTABLE);
    }

    protected void assertArgument(String value) {
        String commandLine=openCoverCommand.toCommandLine();
        assertNotNull(commandLine);
        assertEquals(EXECUTABLE + "/OpenCover.Console.Exe " + value,commandLine);
    }

    protected void assertArgumentNotPresent(String value) {
        String commandLine=openCoverCommand.toCommandLine();
        
        assertNotNull(commandLine);
        boolean doesNotContain = !commandLine.contains(" value");
        assertTrue("argument should not contain " + value,doesNotContain);
    }
    protected OpenCoverCommand createCommand() {
        OpenCoverCommand openCoverCommand = new OpenCoverCommand();
        openCoverCommand.setInstallDir(EXECUTABLE);
        return openCoverCommand;
    }

    public void assertNoArgument() {
        String commandLine=openCoverCommand.toCommandLine();
        assertNotNull(commandLine);
        assertEquals(EXECUTABLE + "/OpenCover.Console.Exe",commandLine);      
    }

}