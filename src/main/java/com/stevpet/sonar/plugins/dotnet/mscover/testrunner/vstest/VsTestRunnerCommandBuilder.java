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
package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.sonar.api.batch.fs.FileSystem;


import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

public  class VsTestRunnerCommandBuilder {

    private MsCoverConfiguration propertiesHelper;
    private List<String> unitTestAssembliesPath;
    private File testSettingsFile;
    private TestConfigFinder testConfigFinder;
    private VSTestCommand vsTestCommand;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    private AssembliesFinder assembliesFinder;
    protected boolean doCodeCoverage;
	private String testCaseFilter;
    private Pattern testProjectPattern;

    public VsTestRunnerCommandBuilder(MsCoverConfiguration propertiesHelper,
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            FileSystem fileSystem, 
            TestConfigFinder testConfigFinder, VSTestCommand vsTestCommand,
            AssembliesFinder assembliesFinder) {
        this.propertiesHelper = propertiesHelper;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem = fileSystem;
        this.testConfigFinder = testConfigFinder;
        this.vsTestCommand = vsTestCommand;
        this.assembliesFinder = assembliesFinder;
    }


    public VSTestCommand build(boolean doCodeCoverage) {
        requireTestSettings();
        findAssemblies();
        return buildVSTestCommand(doCodeCoverage);
    }


    public String getCoverageXmlPath() {
        String path = propertiesHelper.getUnitTestCoveragePath();
        if (StringUtils.isEmpty(path)) {
            path = fileSystem.workDir().getAbsolutePath() + "/coverage.xml";
        }
        return path;
    }

    private void requireTestSettings() {
        String testSettings = propertiesHelper.getTestSettings();
        testSettingsFile = testConfigFinder.getTestSettingsFileOrDie(
                getSolutionDirectory(), testSettings);
    
    }

    private VSTestCommand buildVSTestCommand(boolean doCodeCoverage) {
        String path=propertiesHelper.getVsTestInstallPath();
        vsTestCommand.setExecutableDir(path);
        vsTestCommand.setTestSettingsFile(testSettingsFile);
        vsTestCommand.setUnitTestAssembliesPath(unitTestAssembliesPath);
        vsTestCommand.setCodeCoverage(doCodeCoverage);
        vsTestCommand.setTestCaseFilter(testCaseFilter);
        String platform = propertiesHelper.getRequiredBuildPlatform();
        vsTestCommand.setPlatform(platform);
        return vsTestCommand;
    }

    private void findAssemblies() {
      Preconditions.checkNotNull(testProjectPattern,"TestProjectPattern not set");
        unitTestAssembliesPath = assembliesFinder.setTestProjectPattern(testProjectPattern).
                findUnitTestAssembliesFromConfig(getSolutionDirectory(),
                        microsoftWindowsEnvironment.getCurrentSolution()
                                .getProjects());
    }

    private File getSolutionDirectory() {
        VisualStudioSolution solution = microsoftWindowsEnvironment
                .getCurrentSolution();
        if (solution == null) {
            throw new IllegalStateException("No current solution");
        }
        return solution.getSolutionDir();
    }


	public void setTestCaseFilter(String testCaseFilter) {
		this.testCaseFilter=testCaseFilter;
	}

    public void setTestProjectPattern(@Nonnull Pattern pattern) {
        this.testProjectPattern = pattern;
        
    }
    
}
