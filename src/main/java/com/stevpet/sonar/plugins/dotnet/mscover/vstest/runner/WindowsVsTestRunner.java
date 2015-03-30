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

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.WindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverProgrammerException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.exceptions.MsCoverInvalidSonarWorkingDir;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;


/**
 * @author stevpet
 *
 */
public class WindowsVsTestRunner implements VsTestRunner {
    private static final Logger LOG = LoggerFactory
            .getLogger(WindowsVsTestRunner.class);
    private MsCoverProperties propertiesHelper ;
    private List<String> unitTestAssembliesPath;
    private String coveragePath;
    private String resultsPath;
    private File testSettingsFile;
    
    private String coverageXmlPath;
    private String sonarPath;
    private boolean doCodeCoverage;
    private String stdOutString;
    private CodeCoverageCommand command = new WindowsCodeCoverageCommand();
    private TestConfigFinder testConfigFinder = new VsTestConfigFinder();
    private AbstractAssembliesFinderFactory assembliesFinderFactory =  new AssembliesFinderFactory();
    private VSTestCommand vsTestCommand = VSTestCommand.create();
    private CommandLineExecutor executor = new WindowsCommandLineExecutor();
    private VSTestStdOutParser vsTestResultsParser = new VSTestStdOutParser();
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    private WindowsVsTestRunner() {
    }
    
    public WindowsVsTestRunner(MsCoverProperties propertiesHelper, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            FileSystem fileSystem) {
        this.propertiesHelper = propertiesHelper;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem=fileSystem;     
    }
    public static VsTestRunner create() {
        return new WindowsVsTestRunner();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setSonarPath(java.lang.String)
     */
    public void setSonarPath(String path) {
        this.sonarPath = path;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getSonarPath()
     */
    public String getSonarPath() {
        return fileSystem.workDir().getAbsolutePath();
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setPropertiesHelper(com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties)
     *
    public void setPropertiesHelper(MsCoverProperties propertiesHelper) {
        this.propertiesHelper = propertiesHelper;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setSolution(org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution)
     */
    public void setSolution(VisualStudioSolution solution) {

    }
    

    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getSolutionDirectory()
     */
    public File getSolutionDirectory() {
        return microsoftWindowsEnvironment.getCurrentSolution().getSolutionDir();
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setDoCodeCoverage(boolean)
     */
    public void setDoCodeCoverage(boolean doCodeCoverage) {
        this.doCodeCoverage=doCodeCoverage;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#shouldRun()
     */
    public boolean shouldRun() {
        boolean shouldRun=propertiesHelper.getRunMode() == RunMode.RUNVSTEST;
        LOG.debug("shouldRun ->{}",shouldRun);
        return shouldRun;
    } 
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#runTests()
     */
    public void runTests() {
        VSTestCommand vsTestCommand=prepareTestCommand();
        executeShellCommand(vsTestCommand);
        getResultPaths();
        if(doCodeCoverage) {
            convertCoverageFileToXml();
        }
    }
    
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#prepareTestCommand()
     */
    public VSTestCommand prepareTestCommand() {
        requireTestSettings();
        requireOutputPath();
        findAssemblies();
        return buildVSTestCommand();     
    }

    /**
     * Converts the .coverage file into an xml file
     */
    
    
    private void convertCoverageFileToXml() {
            command.setSonarPath(sonarPath);
            command.setCoveragePath(coveragePath);
            command.setOutputPath(getCoverageXmlPath());
            command.install();
            executeShellCommand(command);
    }
    private void requireOutputPath() {
            if(StringUtils.isEmpty(coverageXmlPath)) {
                coverageXmlPath = getSolutionDirectory() + "/" + propertiesHelper.getUnitTestCoveragePath();
                setCoverageXmlPath(coverageXmlPath);
            }
            if(StringUtils.isEmpty(coverageXmlPath)) {
                throw new SonarException(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH + " not set ");
            }
    }
    
    private void requireTestSettings() {
        String testSettings = propertiesHelper.getTestSettings();
        testSettingsFile = testConfigFinder.getTestSettingsFileOrDie(getSolutionDirectory(),testSettings);
  
    }

    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getResultPaths()
     */
    public void getResultPaths() {
        vsTestResultsParser.setResults(stdOutString);
        setCoveragePath(vsTestResultsParser.getCoveragePath());
        setResultsPath(vsTestResultsParser.getTestResultsXmlPath());     
    }

    private int executeShellCommand(ShellCommand command) {
        int exitCode= executor.execute(command);
        stdOutString=executor.getStdOut();
        return exitCode;
    }
    
    
    private VSTestCommand buildVSTestCommand() {
        vsTestCommand.setTestSettingsFile(testSettingsFile);
        vsTestCommand.setUnitTestAssembliesPath(unitTestAssembliesPath);
        vsTestCommand.setCodeCoverage(doCodeCoverage);
        String platform=propertiesHelper.getRequiredBuildPlatform();
        vsTestCommand.setPlatform(platform);
        return vsTestCommand;
    }
    

    private void findAssemblies() {
        AssembliesFinder assembliesFinder = assembliesFinderFactory.create(propertiesHelper) ;
        unitTestAssembliesPath=assembliesFinder.findUnitTestAssembliesFromConfig(getSolutionDirectory(), microsoftWindowsEnvironment.getCurrentSolution().getProjects());
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getCoveragePath()
     */
    public String getCoveragePath() {
        return coveragePath;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setCoveragePath(java.lang.String)
     */
    public void setCoveragePath(String coveragePath) {
        this.coveragePath = coveragePath;
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getResultsXmlPath()
     */
    public String getResultsXmlPath() {
        return resultsPath;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setResultsPath(java.lang.String)
     */
    public void setResultsPath(String resultsPath) {
        this.resultsPath = resultsPath;
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getCoverageXmlPath()
     */
    public String getCoverageXmlPath() {
        return fileSystem.workDir().getAbsolutePath() + "/coverage.xml";
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setCoverageXmlPath(java.lang.String)
     */
    public void setCoverageXmlPath(String outputPath) {
        this.coverageXmlPath = outputPath;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#clean()
     */
    
    void setTestConfigFinder(TestConfigFinder testConfigFinder) {
        this.testConfigFinder = testConfigFinder;
    }
    public void clean() {
        if(StringUtils.isEmpty(sonarPath)) {
            throw new MsCoverProgrammerException("sonarPath not set");
        }
        File sonarDir=new File(sonarPath);
        if(!".sonar".equalsIgnoreCase(sonarDir.getName())) {
            throw new MsCoverInvalidSonarWorkingDir(sonarPath);
        }
        File testResultsDir=new File(sonarPath,"TestResults");
        if(testResultsDir.exists()) {
            FileUtils.deleteQuietly(testResultsDir);
        }

    }
    
    /**
     * Set the coveragecommand (testing purposes only)
     * @param command
     */
    void setCoverageCommand(CodeCoverageCommand command) {
        this.command = command;
    }

    /**
     * @param assembliesFinderFactory the assembliesFinderFactory to set
     */
    public void setAssembliesFinderFactory(
            AbstractAssembliesFinderFactory assembliesFinderFactory) {
        this.assembliesFinderFactory = assembliesFinderFactory;
    }

    /**
     * @param vsTestCommand the vsTestCommand to set
     */
    public void setVsTestCommand(VSTestCommand vsTestCommand) {
        this.vsTestCommand = vsTestCommand;
    }

    /**
     * @param commandLineExecutor the executor to set
     */
    public void setExecutor(CommandLineExecutor commandLineExecutor) {
        this.executor = commandLineExecutor;
    }

    /**
     * @param vsTestResultsParser the vsTestResultsParser to set
     */
    public void setVsTestResultsParser(VSTestStdOutParser vsTestResultsParser) {
        this.vsTestResultsParser = vsTestResultsParser;
    }

    public void setPropertiesHelper(MsCoverProperties propertiesHelper) {
        this.propertiesHelper=propertiesHelper;
    }
}
    
