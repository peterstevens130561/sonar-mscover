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
package com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner;

import java.io.File;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.stevpet.sonar.plugins.common.api.ShellCommand;
import com.stevpet.sonar.plugins.common.commandexecutor.CommandLineExecutorWithEvents;
import com.stevpet.sonar.plugins.common.commandexecutor.TimeoutException;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverCommandLineConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultOpenCoverTestRunnerRetriesTest {
    @Mock
    private OpenCoverTestRunner openCoverTestRunner;
    @Mock
    private OpenCoverCommandLineConfiguration openCoverCommandLineConfiguration;
    @Mock
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock
    private OpenCoverCommand openCoverCommand;
    @Mock
    private AssembliesFinder assembliesFinder;
    @Mock
    private VSTestStdOutParser vsTestStdOutParser;

    @Mock
    VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder;
    @Mock
    CommandLineExecutorWithEvents commandLineExecutor;

    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);

        openCoverTestRunner = new DefaultOpenCoverTestRunner(openCoverCommandLineConfiguration, microsoftWindowsEnvironment,
                openCoverCommand, assembliesFinder, vsTestRunnerCommandBuilder, vsTestStdOutParser, commandLineExecutor);
        Pattern pattern = Pattern.compile(".*");
        openCoverTestRunner.setTestProjectPattern(pattern);
        openCoverTestRunner.setCoverageFile(new File("somefile"));
    }

    @Test
    public void illegalRetries() {
        try {
            openCoverTestRunner.setRetries(-1);
            fail("expect IllegalArgumentException");
        } catch (IllegalArgumentException e) {

        }
    }
    
    @Test
    public void definedRetries() {
        openCoverTestRunner.setRetries(1);
        executeWithRetries();
        verify(commandLineExecutor, times(2)).execute(any(ShellCommand.class), anyInt());
    }
    @Test
    public void defaultRetries() {
        executeWithRetries();
        verify(commandLineExecutor, times(4)).execute(any(ShellCommand.class), anyInt());
    }
    


    private void executeWithRetries() {
        when(commandLineExecutor.execute(any(ShellCommand.class), anyInt())).thenThrow(new TimeoutException(null, null));
        try {
            openCoverTestRunner.execute();
            fail("expect IllegalStateException");
        } catch (IllegalStateException e) {

        }
    }
}
