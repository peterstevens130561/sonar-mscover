package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.command.StreamConsumer;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper.RunMode;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;

public class VsTestRunner {
    private static final Logger LOG = LoggerFactory
            .getLogger(VsTestRunner.class);
    private PropertiesHelper propertiesHelper ;
    private File solutionDirectory ;
    private List<String> unitTestAssembliesPath;
    private StreamConsumer stdOut;
    private StreamConsumer stdErr;
    private String coveragePath;
    private String resultsPath;
    private File testSettingsFile;
    
    private String outputPath;
    private String sonarPath;
    private boolean doCodeCoverage;
    private List<VisualStudioProject> projects;
    
    private VsTestRunner() {
    }
    
    public static VsTestRunner create() {
        return new VsTestRunner();
    }

    public void setSonarPath(String path) {
        this.sonarPath = path;
    }
    
    public String getSonarPath() {
        return sonarPath;
    }
    public void setPropertiesHelper(PropertiesHelper propertiesHelper) {
        this.propertiesHelper = propertiesHelper;
    }
    
    public void setSolution(VisualStudioSolution solution) {
        this.solutionDirectory = solution.getSolutionDir();
        this.projects = solution.getProjects();
    }
    
    @Deprecated
    public void setSolutionDirectory(File solutionDirectory) {
        this.solutionDirectory = solutionDirectory;
    }
    
    public File getSolutionDirectory() {
        return solutionDirectory;
    }
    
    public void setDoCodeCoverage(boolean doCodeCoverage) {
        this.doCodeCoverage=doCodeCoverage;
    }
    public boolean shouldRun() {
        boolean shouldRun=propertiesHelper.getRunMode() == RunMode.RUNVSTEST;
        LOG.debug("shouldRun ->{}",shouldRun);
        return shouldRun;
    } 
    /**
     * if mode is set to runvstest the runner will attempt to run test & obtain coverage
     * information. Make sure all properties are set:
     * -solutionDirectory
     * -outputPath
     * Result is the .cover file
     * @return
     */
    public void runTests() {
        VSTestCommand vsTestCommand=prepareTestCommand();
        executeShellCommand(vsTestCommand);
        getResultPaths();
        if(doCodeCoverage) {
            convertCoverageFileToXml();
        }
    }
    
    
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
            CodeCoverageCommand command = new CodeCoverageCommand();
            command.setSonarPath(sonarPath);
            command.setCoveragePath(coveragePath);
            command.setOutputPath(getCoverageXmlPath());
            command.install();
            executeShellCommand(command);
    }
    private void requireOutputPath() {
            if(StringUtils.isEmpty(outputPath)) {
                outputPath = solutionDirectory + "/" + propertiesHelper.getUnitTestCoveragePath();
                setCoverageXmlPath(outputPath);
            }
            if(StringUtils.isEmpty(outputPath)) {
                throw new SonarException(PropertiesHelper.MSCOVER_UNIT_COVERAGEXML_PATH + " not set ");
            }
    }
    
    private void requireTestSettings() {
        String testSettings = propertiesHelper.getTestSettings();
        TestConfigFinder configFinder = new VsTestConfigFinder(solutionDirectory);
        testSettingsFile = configFinder.getTestSettingsFileOrDie(testSettings);
  
    }

    
    /**
     * parse test log to get paths to result files
     */
    public void getResultPaths() {
        VSTestStdOutParser vsTestResults = new VSTestStdOutParser();
        vsTestResults.setResults(stdOut.toString());
        setCoveragePath(vsTestResults.getCoveragePath());
        setResultsPath(vsTestResults.getTestResultsXmlPath());     
    }

    private int executeShellCommand(ShellCommand command) {
        stdOut = new StringStreamConsumer();
        stdErr = new StringStreamConsumer();
        long timeOut = (long)(30 * 60000);
        LOG.info(command.toCommandLine());
        int exitCode = CommandExecutor.create().execute(command.toCommand(),stdOut,stdErr, timeOut);
        if(exitCode!=0 && exitCode !=1) {
            String msg=command.toCommandLine() + " failed with exitCode " + exitCode;
            LOG.error(stdErr.toString());
            throw new SonarException(msg);
        }   
        return exitCode;
    }
    
    /**
     * run the test. if the tests were done, stdOut holds the log data
     */
    private VSTestCommand buildVSTestCommand() {
        VSTestCommand vsTestCommand = VSTestCommand.create();
        vsTestCommand.setTestSettingsPath(testSettingsFile.getAbsolutePath());
        vsTestCommand.setUnitTestAssembliesPath(unitTestAssembliesPath);
        vsTestCommand.setCodeCoverage(doCodeCoverage);
        String platform=propertiesHelper.getRequiredBuildPlatform();
        vsTestCommand.setPlatform(platform);
        return vsTestCommand;

    }
    

    private void findAssemblies() {
        AssembliesFinder assembliesFinder = AssembliesFinder.create(propertiesHelper) ;
        unitTestAssembliesPath=assembliesFinder.findAssembliesFromConfig(solutionDirectory, projects);
    }

    /**
     * gets the path to the .cover file
     * @return
     */
    public String getCoveragePath() {
        return coveragePath;
    }
    
    /**
     * sets the path to the .cover file
     * @param coveragePath
     */
    public void setCoveragePath(String coveragePath) {
        this.coveragePath = coveragePath;
    }


    public String getResultsXmlPath() {
        return resultsPath;
    }
    public void setResultsPath(String resultsPath) {
        this.resultsPath = resultsPath;
    }


    public String getCoverageXmlPath() {
        return outputPath;
    }
    public void setCoverageXmlPath(String outputPath) {
        this.outputPath = outputPath;
    }

    class StringStreamConsumer implements StreamConsumer{
        private StringBuilder log ;
        StringStreamConsumer() {
            log = new StringBuilder();
        }

    public void consumeLine(String line) {
        LOG.info(line);
        log.append(line);
        log.append("\r\n");  
    }

    @Override
    public String toString() {
        return log.toString();
    }
    }









}
