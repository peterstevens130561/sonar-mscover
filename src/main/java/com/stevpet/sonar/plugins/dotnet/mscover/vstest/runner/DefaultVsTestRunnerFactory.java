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
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class DefaultVsTestRunnerFactory implements AbstractVsTestRunnerFactory {
    private static Logger LOG = LoggerFactory
            .getLogger(DefaultVsTestRunnerFactory.class);


    /**
     * Create the basic unit testrunner: - path to the executable - solution
     * directory - coverage path - log for test results
     * 
     * Only remaining thing is to set code coverage
     * 
     * @param propertiesHelper
     * @param moduleFileSystem
     * @param microsoftWindowsEnvironment
     *            - directory that holds the solution
     * @param vsTestStdOutParser
     * @param testEnvironment 
     * @param assembliesFinderFactory
     * @return
     */
    public VsTestRunner createBasicTestRunnner(
            MsCoverProperties propertiesHelper, FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            VSTestCommand vsTestCommand,
            CommandLineExecutor commandLineExecutor,
            VSTestStdOutParser vsTestStdOutParser,
            AssembliesFinder assembliesFinder, VsTestEnvironment testEnvironment) {

        WindowsCodeCoverageCommand codeCoverageCommand = new WindowsCodeCoverageCommand();
        TestConfigFinder testConfigFinder = new VsTestConfigFinder();
        VsTestRunnerCommandBuilder vsTestRunnerCommandBuilder = new VsTestRunnerCommandBuilder(propertiesHelper,
                microsoftWindowsEnvironment,
                fileSystem,testConfigFinder,vsTestCommand,assembliesFinder);
        
        VsTestRunner unitTestRunner = new WindowsVsTestRunner(fileSystem, codeCoverageCommand,
                testConfigFinder, vsTestCommand, commandLineExecutor,
                vsTestStdOutParser, vsTestRunnerCommandBuilder,testEnvironment);
        return unitTestRunner;
    }

    @Override
    @Deprecated
    public VsTestRunner createBasicTestRunnner(
            MsCoverProperties propertiesHelper, FileSystem fileSystem,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        // TODO Auto-generated method stub
        VSTestCommand vsTestCommand = VSTestCommand.create();
        CommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor();
        VSTestStdOutParser vsTestStdOutParser = new VSTestStdOutParser();
        AssembliesFinder assembliesFinder = new DefaultAssembliesFinder(propertiesHelper);
        return this.createBasicTestRunnner(propertiesHelper, fileSystem,
                microsoftWindowsEnvironment, vsTestCommand,
                commandLineExecutor, vsTestStdOutParser,
                assembliesFinder);
    }

}
