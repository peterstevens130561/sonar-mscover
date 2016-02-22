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
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.common.api.ShellCommand;
import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.BinaryCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.VsTestCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

/**
 * @author stevpet
 * 
 */
public class VsTestUnitTestRunner implements TestRunner {
    static final Logger LOG = LoggerFactory
            .getLogger(VsTestUnitTestRunner.class);
    protected VSTestStdOutParser vsTestStdOutParser;
    private String coveragePath;
    private String stdOutString;
    private CommandLineExecutor executor;
    private String resultsPath;
    private VsTestRunnerCommandBuilder commandBuilder;
    private VsTestEnvironment testEnvironment;
    private BinaryCoverageToXmlConverter coverageToXmlConverter;
    private Pattern testProjectPattern;


    public VsTestUnitTestRunner(BinaryCoverageToXmlConverter coverageToXmlConverter,
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
    
    public static  VsTestUnitTestRunner create(
			VsTestEnvironment testEnvironment,
			FileSystem fileSystem,
			MsCoverConfiguration msCoverConfiguration, MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
		return new VsTestUnitTestRunner(
				new VsTestCoverageToXmlConverter(fileSystem), 
				new VSTestStdOutParser(), 
				new VsTestRunnerCommandBuilder(
						msCoverConfiguration,
						microsoftWindowsEnvironment,
						fileSystem,
						new VsTestConfigFinder(),
						new VSTestCommand(),
						new DefaultAssembliesFinder(msCoverConfiguration)), 
						new LockedWindowsCommandLineExecutor(
								new DefaultProcessLock()
								), 
						testEnvironment);
	}
    
    public void execute() {
        commandBuilder.setTestProjectPattern(testProjectPattern);
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

	@Override
	public void setCoverageFile(File coverageFile) {
		throw new NotImplementedException("setCoverageFile");
	}

    @Override
    public void setTestProjectPattern(@Nonnull Pattern pattern) {
        this.testProjectPattern=pattern;
    }

}
