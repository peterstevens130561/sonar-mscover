package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;

public  class VsTestRunnerCommandBuilder {

    private MsCoverProperties propertiesHelper;
    private List<String> unitTestAssembliesPath;
    private File testSettingsFile;
    private TestConfigFinder testConfigFinder;
    private VSTestCommand vsTestCommand;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    private AssembliesFinder assembliesFinder;
    protected boolean doCodeCoverage;

    public VsTestRunnerCommandBuilder(MsCoverProperties propertiesHelper,
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
        vsTestCommand.setTestSettingsFile(testSettingsFile);
        vsTestCommand.setUnitTestAssembliesPath(unitTestAssembliesPath);
        vsTestCommand.setCodeCoverage(doCodeCoverage);
        String platform = propertiesHelper.getRequiredBuildPlatform();
        vsTestCommand.setPlatform(platform);
        return vsTestCommand;
    }

    private void findAssemblies() {
        unitTestAssembliesPath = assembliesFinder
                .findUnitTestAssembliesFromConfig(getSolutionDirectory(),
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
    
}
