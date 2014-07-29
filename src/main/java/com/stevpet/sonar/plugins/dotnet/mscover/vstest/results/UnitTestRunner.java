package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

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

public class UnitTestRunner {
    private static final Logger LOG = LoggerFactory
            .getLogger(UnitTestRunner.class);
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
    
    private UnitTestRunner() {
    }
    
    public static UnitTestRunner create() {
        return new UnitTestRunner();
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
        VSTestOutputParser vsTestResults = new VSTestOutputParser();
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
        return vsTestCommand;

    }
    
    /**
     * Gets the unittest assemblies. Expect to find at least one, so if none found throw
     * a sonar exception, as continuing is useless
     */
    private void findTestAssembliesFromProperty() {
        String assembliesPattern = propertiesHelper.getUnitTestsAssemblies();
        if(StringUtils.isEmpty(assembliesPattern)) {
            throw new SonarException(PropertiesHelper.MSCOVER_UNITTEST_ASSEMBLIES + " not set, required though when using this mode");
        }
        AssembliesFinder assembliesFinder = AssembliesFinder.create(propertiesHelper) ;
        assembliesFinder.setPattern(assembliesPattern);
        unitTestAssembliesPath = assembliesFinder.findAssemblies(solutionDirectory);
        if(unitTestAssembliesPath.isEmpty()) {
            throw new SonarException(" No unittest assemblies found with pattern '" + assembliesPattern + "'");
        }
        
    }
    
    private void findAssemblies() {
        String assembliesPattern = propertiesHelper.getUnitTestsAssemblies();
        if(StringUtils.isEmpty(assembliesPattern)) {
            LOG.debug(PropertiesHelper.MSCOVER_UNITTEST_ASSEMBLIES + " undefined, will use projects to find test projects");
            AssembliesFinder assembliesFinder = AssembliesFinder.create(propertiesHelper) ;
            unitTestAssembliesPath = assembliesFinder.findTestProjects(projects);
            if(unitTestAssembliesPath.isEmpty()) {
                LOG.warn(" no test projects found");
            }
        }  else {      
            findTestAssembliesFromProperty();
        }
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
