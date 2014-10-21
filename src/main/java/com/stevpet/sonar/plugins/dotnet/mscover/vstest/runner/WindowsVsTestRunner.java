package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
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
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
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
    private File solutionDirectory ;
    private List<String> unitTestAssembliesPath;
    private String coveragePath;
    private String resultsPath;
    private File testSettingsFile;
    
    private String outputPath;
    private String sonarPath;
    private boolean doCodeCoverage;
    private List<VisualStudioProject> projects;
    private String stdOutString;
    private CodeCoverageCommand command = new WindowsCodeCoverageCommand();
    private TestConfigFinder testConfigFinder = new VsTestConfigFinder();
    private AssembliesFinder assembliesFinder;
    private AbstractAssembliesFinderFactory assembliesFinderFactory =  new AssembliesFinderFactory();
    private WindowsVsTestRunner() {
    }
    
    public static VsTestRunner create() {
        return new WindowsVsTestRunner();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setSonarPath(java.lang.String)
     */
    @Override
    public void setSonarPath(String path) {
        this.sonarPath = path;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getSonarPath()
     */
    @Override
    public String getSonarPath() {
        return sonarPath;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setPropertiesHelper(com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties)
     */
    @Override
    public void setPropertiesHelper(MsCoverProperties propertiesHelper) {
        this.propertiesHelper = propertiesHelper;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setSolution(org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution)
     */
    @Override
    public void setSolution(VisualStudioSolution solution) {
        this.solutionDirectory = solution.getSolutionDir();
        this.projects = solution.getProjects();
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setSolutionDirectory(java.io.File)
     */
    @Override
    @Deprecated
    public void setSolutionDirectory(File solutionDirectory) {
        this.solutionDirectory = solutionDirectory;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getSolutionDirectory()
     */
    @Override
    public File getSolutionDirectory() {
        return solutionDirectory;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setDoCodeCoverage(boolean)
     */
    @Override
    public void setDoCodeCoverage(boolean doCodeCoverage) {
        this.doCodeCoverage=doCodeCoverage;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#shouldRun()
     */
    @Override
    public boolean shouldRun() {
        boolean shouldRun=propertiesHelper.getRunMode() == RunMode.RUNVSTEST;
        LOG.debug("shouldRun ->{}",shouldRun);
        return shouldRun;
    } 
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#runTests()
     */
    @Override
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
    @Override
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
        testSettingsFile = testConfigFinder.getTestSettingsFileOrDie(solutionDirectory,testSettings);
  
    }

    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getResultPaths()
     */
    @Override
    public void getResultPaths() {
        VSTestStdOutParser vsTestResults = new VSTestStdOutParser();
        vsTestResults.setResults(stdOutString);
        setCoveragePath(vsTestResults.getCoveragePath());
        setResultsPath(vsTestResults.getTestResultsXmlPath());     
    }

    private int executeShellCommand(ShellCommand command) {
        CommandLineExecutor executor = new WindowsCommandLineExecutor();
        int exitCode= executor.execute(command);
        stdOutString=executor.getStdOut();
        return exitCode;
    }
    
    
    private VSTestCommand buildVSTestCommand() {
        VSTestCommand vsTestCommand = VSTestCommand.create();
        vsTestCommand.setTestSettingsFile(testSettingsFile);
        vsTestCommand.setUnitTestAssembliesPath(unitTestAssembliesPath);
        vsTestCommand.setCodeCoverage(doCodeCoverage);
        String platform=propertiesHelper.getRequiredBuildPlatform();
        vsTestCommand.setPlatform(platform);
        return vsTestCommand;
    }
    

    private void findAssemblies() {

        assembliesFinder = assembliesFinderFactory.create(propertiesHelper) ;
        unitTestAssembliesPath=assembliesFinder.findUnitTestAssembliesFromConfig(solutionDirectory, projects);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getCoveragePath()
     */
    @Override
    public String getCoveragePath() {
        return coveragePath;
    }
    
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setCoveragePath(java.lang.String)
     */
    @Override
    public void setCoveragePath(String coveragePath) {
        this.coveragePath = coveragePath;
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getResultsXmlPath()
     */
    @Override
    public String getResultsXmlPath() {
        return resultsPath;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setResultsPath(java.lang.String)
     */
    @Override
    public void setResultsPath(String resultsPath) {
        this.resultsPath = resultsPath;
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#getCoverageXmlPath()
     */
    @Override
    public String getCoverageXmlPath() {
        return outputPath;
    }
    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#setCoverageXmlPath(java.lang.String)
     */
    @Override
    public void setCoverageXmlPath(String outputPath) {
        this.outputPath = outputPath;
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner#clean()
     */
    
    void setTestConfigFinder(TestConfigFinder testConfigFinder) {
        this.testConfigFinder = testConfigFinder;
    }
    @Override
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
}
    
