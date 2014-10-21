package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;

public class WindowsVsTestRunnerTest {

    private VsTestRunner runner;
    private TestConfigFinder testConfigFinder;
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
        createRunner();
        PropertiesHelper propertiesHelper = mock(PropertiesHelper.class);
        runner.setPropertiesHelper(propertiesHelper);
        
        testConfigFinder=mock(TestConfigFinder.class);
        ((WindowsVsTestRunner)runner).setTestConfigFinder(testConfigFinder);
        VisualStudioSolution solution = mock(VisualStudioSolution.class);
        runner.setSolution(solution);
        
        AssembliesFinder assembliesFinder = mock(AssembliesFinder.class);
        AbstractAssembliesFinderFactory factory = mock(AbstractAssembliesFinderFactory.class);
        when(factory.create(any(PropertiesHelper.class))).thenReturn(assembliesFinder);
        ((WindowsVsTestRunner)runner).setAssembliesFinderFactory(factory);
        
        VSTestCommand vsTestCommand = mock(VSTestCommand.class);
        ((WindowsVsTestRunner)runner).setVsTestCommand(vsTestCommand);
        
        CommandLineExecutor commandLineExecutor= mock(CommandLineExecutor.class);
        ((WindowsVsTestRunner)runner).setExecutor(commandLineExecutor);
        
        VSTestStdOutParser vsTestResultsParser = mock(VSTestStdOutParser.class);
        ((WindowsVsTestRunner)runner).setVsTestResultsParser(vsTestResultsParser);
        
        List<String> unitTestPaths= new ArrayList();
        when(assembliesFinder.findUnitTestAssembliesFromConfig(any(File.class), anyList())).thenReturn(unitTestPaths);
        String sonarWorkingDirectory="bogus";
        String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
        runner.setCoverageXmlPath(coverageXmlPath);
        runner.setSonarPath(sonarWorkingDirectory);
        runner.runTests();
        
        //Testing happens here
    }

    private void expectRunnerIsValid() {
        assertNotNull(runner);
        assertTrue(runner instanceof WindowsVsTestRunner);
    }

    private void createRunner() {
        runner=WindowsVsTestRunner.create();
    }
}
