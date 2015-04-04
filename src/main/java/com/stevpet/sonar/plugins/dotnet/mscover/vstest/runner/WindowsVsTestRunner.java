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

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;

/**
 * @author stevpet
 * 
 */
public class WindowsVsTestRunner  implements VsTestRunner {
    static final Logger LOG = LoggerFactory
            .getLogger(WindowsVsTestRunner.class);
    protected VSTestStdOutParser vsTestStdOutParser;
    private String coveragePath;
    private CodeCoverageCommand codeCoverageCommand;
    private  String stdOutString;
    private CommandLineExecutor executor;
    private FileSystem fileSystem;
    private String resultsPath;
    private boolean doCodeCoverage;
    private VsTestRunnerCommandBuilder commandBuilder;
    private VsTestEnvironment testEnvironment;
    
    public WindowsVsTestRunner(
            FileSystem fileSystem, 
            CodeCoverageCommand codeCoverageCommand,
            TestConfigFinder testConfigFinder, 
            VSTestCommand vsTestCommand,
            CommandLineExecutor commandLineExecutor,
            VSTestStdOutParser vsTestStdOutParser,
            VsTestRunnerCommandBuilder commandBuilder,
            VsTestEnvironment testEnvironment) {
        this.commandBuilder = commandBuilder;
        this.fileSystem = fileSystem;
        this.codeCoverageCommand = codeCoverageCommand;
        this.executor = commandLineExecutor;
        this.vsTestStdOutParser = vsTestStdOutParser;
        this.testEnvironment=testEnvironment;
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#runTests
     * ()
     */
    public void execute() {
        ShellCommand vsTestCommand = commandBuilder.build(doCodeCoverage);
        executeShellCommand(vsTestCommand);
        getResultPaths();
        if (doCodeCoverage) {
            convertCoverageFileToXml();
        }
    }

    /**
     * Converts the .coverage file into an xml file
     */
    protected void convertCoverageFileToXml() {
        String sonarPath = fileSystem.workDir().getAbsolutePath();
        codeCoverageCommand.setSonarPath(sonarPath);
        codeCoverageCommand.setCoveragePath(coveragePath);
        codeCoverageCommand.setOutputPath(getCoverageXmlPath());
        codeCoverageCommand.install();
        executeShellCommand(codeCoverageCommand);
    }

    int executeShellCommand(ShellCommand command) {
    	boolean isShell = command instanceof ShellCommand;
        int exitCode = executor.execute(command);
        stdOutString = executor.getStdOut();
        return exitCode;
    }

    protected void getResultPaths() {
        vsTestStdOutParser.setResults(stdOutString);
        this.coveragePath=vsTestStdOutParser.getCoveragePath();
        this.resultsPath = vsTestStdOutParser.getTestResultsXmlPath();
    }
    
    public String getResultsXmlPath() {
        return resultsPath;
    }

    @Override
    public void setDoCodeCoverage(boolean doCodeCoverage) {
        this.doCodeCoverage=doCodeCoverage;
    }

    @Override
    public boolean shouldRun() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public String getCoverageXmlPath() {
        // TODO Auto-generated method stub
        return testEnvironment.getXmlCoveragePath();
    }


}
