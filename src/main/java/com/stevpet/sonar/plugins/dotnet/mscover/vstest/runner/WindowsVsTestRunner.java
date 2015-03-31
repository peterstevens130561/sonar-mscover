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
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand;
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
public class WindowsVsTestRunner extends  VsTestRunnerCommandBuilder {
    private static final Logger LOG = LoggerFactory
            .getLogger(WindowsVsTestRunner.class);
    private MsCoverProperties propertiesHelper ;
    private List<String> unitTestAssembliesPath;
    private String coveragePath;
    private String resultsPath;
    private File testSettingsFile;
    
    private String sonarPath;
    private boolean doCodeCoverage;
    private String stdOutString;
    private CodeCoverageCommand codeCoverageCommand;
    private TestConfigFinder testConfigFinder;
    private AbstractAssembliesFinderFactory assembliesFinderFactory =  new AssembliesFinderFactory();
    private VSTestCommand vsTestCommand;
    private CommandLineExecutor executor = new WindowsCommandLineExecutor();
    private VSTestStdOutParser vsTestStdOutParser;
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private FileSystem fileSystem;
    private WindowsVsTestRunner() {
    }
    
    public WindowsVsTestRunner(MsCoverProperties propertiesHelper, 
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment, 
            FileSystem fileSystem,
            CodeCoverageCommand codeCoverageCommand,
            TestConfigFinder testConfigFinder,
            VSTestCommand vsTestCommand, 
            CommandLineExecutor commandLineExecutor, 
            VSTestStdOutParser vsTestStdOutParser) {
        this.propertiesHelper = propertiesHelper;
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.fileSystem=fileSystem;
        this.codeCoverageCommand=codeCoverageCommand;
        this.testConfigFinder=testConfigFinder;
        this.vsTestCommand=vsTestCommand;
                this.executor=commandLineExecutor;
                this.vsTestStdOutParser=vsTestStdOutParser;
    }
    public static VsTestRunner create() {
        return new WindowsVsTestRunner();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setSonarPath(java.lang.String)
     */

        
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getSolutionDirectory()
     */
    private File getSolutionDirectory() {
        VisualStudioSolution solution=microsoftWindowsEnvironment.getCurrentSolution();
        if(solution==null) {
            throw new SonarException("No current solution");
        }
        return solution.getSolutionDir();
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
    public void execute() {
        VSTestCommand vsTestCommand=build();
        executeShellCommand(vsTestCommand);
        getResultPaths();
        if(doCodeCoverage) {
            convertCoverageFileToXml();
        }
    }
    
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#prepareTestCommand()
     */
    public VSTestCommand build() {
        requireTestSettings();
        findAssemblies();
        return buildVSTestCommand();     
    }

    /**
     * Converts the .coverage file into an xml file
     */
    
    
    private void convertCoverageFileToXml() {
            String sonarPath=fileSystem.workDir().getAbsolutePath();
            codeCoverageCommand.setSonarPath(sonarPath);
            codeCoverageCommand.setCoveragePath(coveragePath);
            codeCoverageCommand.setOutputPath(getCoverageXmlPath());
            codeCoverageCommand.install();
            executeShellCommand(codeCoverageCommand);
    }
   
    public String getCoverageXmlPath() {
        String path=propertiesHelper.getUnitTestCoveragePath() ;
        if(StringUtils.isEmpty(path)) {
            path=fileSystem.workDir().getAbsolutePath() + "/coverage.xml";
        }
        return path;
    }


    private void requireTestSettings() {
        String testSettings = propertiesHelper.getTestSettings();
        testSettingsFile = testConfigFinder.getTestSettingsFileOrDie(getSolutionDirectory(),testSettings);
  
    }

    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getResultPaths()
     */
    private void getResultPaths() {
        vsTestStdOutParser.setResults(stdOutString);
        setCoveragePath(vsTestStdOutParser.getCoveragePath());
        this.resultsPath = vsTestStdOutParser.getTestResultsXmlPath();    
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
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setCoveragePath(java.lang.String)
     */
    private void setCoveragePath(String coveragePath) {
        this.coveragePath = coveragePath;
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getResultsXmlPath()
     */
    public String getResultsXmlPath() {
        return resultsPath;
    }


    void setTestConfigFinder(TestConfigFinder testConfigFinder) {
        this.testConfigFinder = testConfigFinder;
    }
    public void clean() {
        File sonarDir=fileSystem.workDir();
        if(sonarDir==null) {
            throw new MsCoverProgrammerException("sonarPath not set");
        }
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
        this.codeCoverageCommand = command;
    }

    /**
     * @param assembliesFinderFactory the assembliesFinderFactory to set
     */
    public void setAssembliesFinderFactory(
            AbstractAssembliesFinderFactory assembliesFinderFactory) {
        this.assembliesFinderFactory = assembliesFinderFactory;
    }

}
    
