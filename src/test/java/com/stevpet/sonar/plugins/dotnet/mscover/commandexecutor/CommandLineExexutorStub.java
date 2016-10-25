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
package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import com.stevpet.sonar.plugins.common.api.ShellCommand;
import com.stevpet.sonar.plugins.common.commandexecutor.CommandLineExecutorWithEvents;
import com.stevpet.sonar.plugins.common.commandexecutor.LineReceivedListener;

public class CommandLineExexutorStub implements CommandLineExecutorWithEvents {

    String commandLine ;
    private int timeoutMinutes;
    @Override
    public int execute(ShellCommand command) {
        if(command !=null) {
            commandLine=command.toCommandLine();
        }
        return 0;
    }

    @Override
    public String getStdOut() {
        return null;
    }

    @Override
    public String getStdErr() {
        return null;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public int getTimeoutMinutes() {
        return timeoutMinutes;
    }
    @Override
    public int execute(ShellCommand command, int timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
        if(command !=null) {
            commandLine=command.toCommandLine();
        }
        return 0;
    }

    @Override
    public void addLineReceivedListener(LineReceivedListener listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeLineReceivedListener(LineReceivedListener listener) {
        // TODO Auto-generated method stub
        
    }

}
