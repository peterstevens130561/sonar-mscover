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
package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.sonar.api.utils.command.Command;
import org.sonar.api.utils.command.StreamConsumer;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;

import com.stevpet.sonar.plugins.common.api.ShellCommand;
import com.stevpet.sonar.plugins.common.commandexecutor.CommandExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.CommandExecutors;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

import static org.mockito.Mockito.when;
public class WindowsCommandLineExecutor_ExecuteTest {
    @Mock private CommandExecutor commandExecutor ;
    @Mock private CommandExecutors commandExecutors;
    @Mock private ShellCommand shellCommand;

    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void executeTestPass() {
        when(commandExecutors.create()).thenReturn(commandExecutor);
        WindowsCommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor(commandExecutors);
        commandLineExecutor.execute(shellCommand);
        verify(commandExecutor,times(1)).execute(any(Command.class), any(StreamConsumer.class), any(StreamConsumer.class), anyLong());
    }
    
    @Test
    public void executeTestFail() {
        when(commandExecutors.create()).thenReturn(commandExecutor);
        when(commandExecutor.execute(any(Command.class), any(StreamConsumer.class), any(StreamConsumer.class), anyLong())).thenReturn(2);
        WindowsCommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor(commandExecutors);
        try { 
            commandLineExecutor.execute(shellCommand);
        } catch (IllegalStateException e) {
            return;
        }
        fail("expected IllegalStateException");
    }
}
