/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.eq;

import org.mockito.ArgumentCaptor;
import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class CommandLineExecutorMock extends GenericClassMock<CommandLineExecutor> {
    ArgumentCaptor<ShellCommand> argument = ArgumentCaptor.forClass(ShellCommand.class);
    public CommandLineExecutorMock() {
        super(CommandLineExecutor.class);
    }

    public void givenGetStdOut(String stdOut) {
        when(instance.getStdOut()).thenReturn(stdOut);
    }

    public void thenCommandLine(String commandLine) {
        ShellCommand fakeCommand = new FakeCommand(commandLine);
        verify(instance).execute(eq(fakeCommand));
    }
    
    private class FakeCommand implements ShellCommand {
        private String commandLine;
        FakeCommand(String commandLine) {
            this.commandLine = commandLine;
        }
        @Override
        public String toCommandLine() {
            return commandLine;
        }
        @Override
        public Command toCommand() {
            return null;
        }
        
        @Override
        public
        boolean equals(Object other) {
            if(other==null) {
                return false ;
            }
            if(!(other instanceof ShellCommand)) {
                return false ;
            }
            ShellCommand otherShellCommand = (ShellCommand) other ;
            String otherLine=otherShellCommand.toCommandLine();
            boolean result= otherLine.equals(commandLine);
            return result;
        }
        
        @Override 
        public String toString() {
        	return commandLine;
        }
    }
}
