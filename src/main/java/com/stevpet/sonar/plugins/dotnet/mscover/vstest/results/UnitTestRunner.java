package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.api.utils.command.StreamConsumer;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class UnitTestRunner {
    private static final Logger LOG = LoggerFactory
            .getLogger(UnitTestRunner.class);
    private PropertiesHelper propertiesHelper ;
    private File solutionDirectory ;
    private List<String> unitTestAssembliesPath;
    private String testSettingsPath;
    private StreamConsumer stdOut;
    private StreamConsumer stdErr;
    private String coveragePath;
    private String resultsPath;
    
    private String outputPath;
    
    private UnitTestRunner() {
    }
    
    public static UnitTestRunner create() {
        return new UnitTestRunner();
    }

    public void setPropertiesHelper(PropertiesHelper propertiesHelper) {
        this.propertiesHelper = propertiesHelper;
    }
    
    public void setSolutionDirectory(File solutionDirectory) {
        this.solutionDirectory = solutionDirectory;
    }
    
    public boolean shouldRun() {
        boolean shouldRun="runvstest".equals(propertiesHelper.getMode());
        LOG.debug("shouldRun ->{}",shouldRun);
        return shouldRun;
    } 
    /**
     * if mode is set to runvstest the runner will attempt to run test & obtain coverage
     * information. Make sure all properties are set:
     * -solutionDirectory
     * -outputPath
     * @return
     */
    public boolean runTests() {
        requireTestSettings();
        requireOutputPath();
        findAssemblies();
        runVsTest();
        getResultPaths();
        runCodeCoverage();
        return true;
 
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
        if(StringUtils.isEmpty(testSettings)) {
            throw new SonarException(PropertiesHelper.MSCOVER_TESTSETTINGS + " not set, required though when using this mode");
        }
        testSettingsPath = solutionDirectory.getAbsolutePath() + "\\" + testSettings;
        File testSettingsFile = new File(testSettingsPath);
        if(!testSettingsFile.exists()) {
            throw new SonarException(PropertiesHelper.MSCOVER_TESTSETTINGS + " file " + testSettingsPath + " does not exist");
        }      
    }
    
    private void getResultPaths() {
        VSTestResults vsTestResults = new VSTestResults();
        vsTestResults.setResults(stdOut.toString());
        setCoveragePath(vsTestResults.getCoveragePath());
        setResultsPath(vsTestResults.getResultsPath());
        
    }
    private void runCodeCoverage() {
        CodeCoverageCommand command = new CodeCoverageCommand();
        command.setCoveragePath(coveragePath);
        command.setOutputPath(getOutputPath());
        executeShellCommand(command);
   
 
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
    private void runVsTest() {
        VSTestCommand vsTestCommand = VSTestCommand.create();
        vsTestCommand.setTestSettingsPath(testSettingsPath);
        vsTestCommand.setUnitTestAssembliesPath(unitTestAssembliesPath);
        executeShellCommand(vsTestCommand);

    }
    
    /**
     * Gets the unittest assemblies. Expect to find at least one, so if none found throw
     * a sonar exception, as continuing is useless
     */
    private void findAssemblies() {
        String assembliesPattern = propertiesHelper.getUnitTestsAssemblies();
        if(StringUtils.isEmpty(assembliesPattern)) {
            throw new SonarException(PropertiesHelper.MSCOVER_UNITTEST_ASSEMBLIES + " not set, required though when using this mode");
        }
        AssembliesFinder assembliesFinder = AssembliesFinder.create() ;
        assembliesFinder.setPattern(assembliesPattern);
        unitTestAssembliesPath = assembliesFinder.findAssemblies(solutionDirectory);
        if(unitTestAssembliesPath.isEmpty()) {
            throw new SonarException(" No unittest assemblies found with pattern '" + assembliesPattern + "'");
        }
        
    }


    public String getCoveragePath() {
        return coveragePath;
    }
    public void setCoveragePath(String coveragePath) {
        this.coveragePath = coveragePath;
    }


    public String getResultsXmlPath() {
        return resultsPath;
    }
    public void setResultsPath(String resultsPath) {
        this.resultsPath = resultsPath;
    }


    public String getOutputPath() {
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
