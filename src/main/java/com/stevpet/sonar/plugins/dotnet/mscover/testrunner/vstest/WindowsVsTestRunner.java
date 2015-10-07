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
package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.CoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

/**
 * @author stevpet
 * 
 */
public class WindowsVsTestRunner implements TestRunner {
    static final Logger LOG = LoggerFactory
            .getLogger(WindowsVsTestRunner.class);
    protected VSTestStdOutParser vsTestStdOutParser;
    private String coveragePath;
    private String stdOutString;
    private CommandLineExecutor executor;
    private String resultsPath;
    private VsTestRunnerCommandBuilder commandBuilder;
    private VsTestEnvironment testEnvironment;
    private CoverageToXmlConverter coverageToXmlConverter;


    public WindowsVsTestRunner(CoverageToXmlConverter coverageToXmlConverter,
            VSTestStdOutParser vsTestStdOutParser,
            VsTestRunnerCommandBuilder commandBuilder,
            CommandLineExecutor commandLineExecutor,
            VsTestEnvironment testEnvironment) {
        this.commandBuilder = commandBuilder;
        this.coverageToXmlConverter = coverageToXmlConverter;
        this.vsTestStdOutParser = vsTestStdOutParser;
        this.executor = commandLineExecutor;
        this.testEnvironment = testEnvironment;
    }

    public void execute() {
        ShellCommand vsTestCommand = commandBuilder.build(true);
        executor.execute(vsTestCommand);
        stdOutString = executor.getStdOut();
        vsTestStdOutParser.setStdOut(stdOutString);
        this.coveragePath = vsTestStdOutParser.getCoveragePath();
        this.resultsPath = vsTestStdOutParser.getTestResultsXmlPath();
        coverageToXmlConverter.convert(
                new File(testEnvironment.getXmlCoveragePath()), new File(
                        coveragePath));
    }

    @Override
    public File getTestResultsFile() {
        return new File(resultsPath);
    }

}
