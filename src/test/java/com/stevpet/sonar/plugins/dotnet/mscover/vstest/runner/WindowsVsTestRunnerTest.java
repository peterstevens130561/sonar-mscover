package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class WindowsVsTestRunnerTest {

    private VsTestRunner runner;
    private TestConfigFinder testConfigFinder;
    private WindowsVsTestRunner windowsVsTestRunner;
    private CommandLineExecutor commandLineExecutor= mock(CommandLineExecutor.class);
    private VSTestCommand vsTestCommand= mock(VSTestCommand.class);
    private VSTestStdOutParser vsTestResultsParser= mock(VSTestStdOutParser.class);
    @Before
    public void before() {
        
    }
    
    @Test
    public void createTestRunner_ExpectValid() {
        createRunner();
        expectRunnerIsValid();
    }
    
    @Test
    public void simpleRunner_verifyInvocation() {

  
        File testSettingsFile=new File("testSettings");
        File solutionDir = null;
        List<String> unitTestAssemblies = new ArrayList<String>() ;
        unitTestAssemblies.add("a");
        String testResult = "Oh jeh";
        String resultsPath = "results.trx";
        createRunner();
        
        givenTestSettingsFile(testSettingsFile);     
        givenSolutionDir(solutionDir);    
        givenUnitTestAssemblies(unitTestAssemblies);
        givenNoSettings();
        givenTestResultsFile(resultsPath);      
        givenVsTestReturns(testResult);
        
        injectCommandLineExecutor();
        injectVsTestCommand();
        injectResultsParser();

        
        String sonarWorkingDirectory="bogus";
        String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
        runner.setCoverageXmlPath(coverageXmlPath);
        runner.setSonarPath(sonarWorkingDirectory);
        runner.runTests();

        assertThatVsTestCommandIsBuilt(testSettingsFile, unitTestAssemblies,
                vsTestCommand);
        
        assertThatCommandLineExecutorIsInvoked(vsTestCommand,
                commandLineExecutor);
        
        assertResultsParserIsInvoked(testResult);
        
    }



    private void injectResultsParser() {
        windowsVsTestRunner.setVsTestResultsParser(vsTestResultsParser);
    }

    private void givenTestResultsFile(String resultsPath) {
        when(vsTestResultsParser.getTestResultsXmlPath()).thenReturn(resultsPath);
    }

    private CommandLineExecutor injectCommandLineExecutor() {
        when(commandLineExecutor.execute(vsTestCommand)).thenReturn(0);
        windowsVsTestRunner.setExecutor(commandLineExecutor);
        return commandLineExecutor;
    }

    private VSTestCommand injectVsTestCommand() {
        windowsVsTestRunner.setVsTestCommand(vsTestCommand);
        return vsTestCommand;
    }

    private void assertThatCommandLineExecutorIsInvoked(
            VSTestCommand vsTestCommand, CommandLineExecutor commandLineExecutor) {
        verify(commandLineExecutor,times(1)).execute(vsTestCommand);
        verify(commandLineExecutor,times(1)).getStdOut();
    }

    private void assertResultsParserIsInvoked(String testResult) {
        verify(vsTestResultsParser,times(1)).getCoveragePath();
        verify(vsTestResultsParser,times(1)).getTestResultsXmlPath();
        verify(vsTestResultsParser,times(1)).setResults(testResult);
        verify(vsTestResultsParser,times(1)).getTestResultsXmlPath();
        verify(vsTestResultsParser,times(1)).getCoveragePath();
    }
    private void assertThatVsTestCommandIsBuilt(File testSettingsFile,
            List<String> unitTestAssemblies, VSTestCommand vsTestCommand) {
        verify(vsTestCommand,times(1)).setUnitTestAssembliesPath(unitTestAssemblies);
        verify(vsTestCommand,times(1)).setTestSettingsFile(testSettingsFile);
        verify(vsTestCommand,times(1)).setCodeCoverage(false);
        verify(vsTestCommand,times(0)).toCommand();
        verify(vsTestCommand,times(0)).toCommandLine();
    }

    private void givenNoSettings() {
        PropertiesHelper propertiesHelper = mock(PropertiesHelper.class);
        runner.setPropertiesHelper(propertiesHelper);
    }

    private void givenVsTestReturns(String testResult) {
        when(commandLineExecutor.getStdOut()).thenReturn(testResult);
    }
    
    private void givenSolutionDir(File solutionDir) {
        VisualStudioSolution solution = mock(VisualStudioSolution.class);
        when(solution.getSolutionDir()).thenReturn(solutionDir);
        runner.setSolution(solution);
    }

    private void givenUnitTestAssemblies(List<String> unitTestAssemblies) {
        AssembliesFinder assembliesFinder = mock(AssembliesFinder.class);
        when(assembliesFinder.findUnitTestAssembliesFromConfig(any(File.class), anyList())).thenReturn(unitTestAssemblies);
        AbstractAssembliesFinderFactory factory = mock(AbstractAssembliesFinderFactory.class);
        when(factory.create(any(PropertiesHelper.class))).thenReturn(assembliesFinder);
        windowsVsTestRunner.setAssembliesFinderFactory(factory);
    }

    private void givenTestSettingsFile(File testSettingsFile) {
        testConfigFinder=mock(TestConfigFinder.class);
        when(testConfigFinder.getTestSettingsFileOrDie(any(File.class),anyString())).thenReturn(testSettingsFile);

        windowsVsTestRunner.setTestConfigFinder(testConfigFinder);
    }

    private void expectRunnerIsValid() {
        assertNotNull(runner);
        assertTrue(runner instanceof WindowsVsTestRunner);
    }

    private void createRunner() {
        runner=WindowsVsTestRunner.create();
        windowsVsTestRunner = (WindowsVsTestRunner)runner;
    }
}
