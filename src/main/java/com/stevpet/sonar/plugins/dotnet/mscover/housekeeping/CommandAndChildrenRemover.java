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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

public class CommandAndChildrenRemover {
    private static Logger LOG = LoggerFactory.getLogger(CommandAndChildrenRemover.class);
    private CommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor();
    private MwicBridge processHelper = new MwicBridge(commandLineExecutor);

    public void cancel(String commandLine) {
        try {
        ProcessesProperties properties = processHelper.getProcessPropertiesForName("opencover.console.exe");
        String openCoverProcessId = properties.getProcessIdOfCommandLine(commandLine);
        
        ProcessesProperties vsTestProperties = processHelper.getProcessPropertiesForName("vstest.console.exe");
        String vsTestId = vsTestProperties.getProcessIdOfChildOf(openCoverProcessId);
        
        ProcessesProperties teProperties = processHelper.getProcessPropertiesForName("TE.ProcessHost.Managed.exe");
        String teId = teProperties.getProcessIdOfChildOf(vsTestId);
        processHelper.killProcessId(teId);
        processHelper.killProcessId(vsTestId);
        processHelper.killProcessId(openCoverProcessId);
        } catch ( Exception e ) {
            LOG.error("Exception thrown during cancelling process {}",e.getMessage());
        }
    }
}
