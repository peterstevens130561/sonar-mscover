package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.SonarException;

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
            throw new SonarException("No current solution");
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
