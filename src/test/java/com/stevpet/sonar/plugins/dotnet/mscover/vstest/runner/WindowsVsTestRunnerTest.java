/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.ConstructorInjection;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.AssembliesFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestUnitTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommandShim;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.BinaryCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioSolution;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class WindowsVsTestRunnerTest {

    private TestRunner runner;
    private TestConfigFinder testConfigFinder=mock(TestConfigFinder.class);
    private CommandLineExecutor commandLineExecutor= mock(CommandLineExecutor.class);
    private VSTestCommand vsTestCommand= mock(VSTestCommand.class);
    private VSTestStdOutParser vsTestResultsParser= mock(VSTestStdOutParser.class);
    private FileSystemMock fileSystemMock = new FileSystemMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private AssembliesFinderMock assembliesFinderMock = new AssembliesFinderMock();
    private VsTestEnvironment testEnvironment;
    @Mock BinaryCoverageToXmlConverter coverageToXmlConverter;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        testEnvironment= new VsTestEnvironment();
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
        testEnvironment.setCoverageXmlPath(".sonar/coverage.xml");
        
        givenTestSettingsFile(testSettingsFile);     
        givenSolutionDir(solutionDir);    
        givenUnitTestAssemblies(unitTestAssemblies);
        givenTestResultsFile(resultsPath);      
        givenVsTestReturns(testResult);

        String sonarWorkingDirectory="bogus";
        //String coverageXmlPath =sonarWorkingDirectory + "/coverage.xml";
        //runner.setCoverageXmlPath(coverageXmlPath);
        when(vsTestResultsParser.getCoveragePath()).thenReturn(sonarWorkingDirectory + "/coverage.xml");
        fileSystemMock.givenWorkDir(sonarWorkingDirectory);
        Pattern pattern=Pattern.compile("somepattern");
        runner.setTestProjectPattern(pattern);
        runner.execute();

        assertThatVsTestCommandIsBuilt(testSettingsFile, unitTestAssemblies,
                vsTestCommand);
        
        assertThatCommandLineExecutorIsInvoked(vsTestCommand,
                commandLineExecutor);
        
        assertResultsParserIsInvoked(testResult);
        
    }


    private void givenTestResultsFile(String resultsPath) {
        when(vsTestResultsParser.getTestResultsXmlPath()).thenReturn(resultsPath);
    }


    private void assertThatCommandLineExecutorIsInvoked(
            VSTestCommand vsTestCommand, CommandLineExecutor commandLineExecutor) {
        verify(commandLineExecutor,times(1)).execute(vsTestCommand);
        verify(commandLineExecutor,times(1)).getStdOut();
    }

    private void assertResultsParserIsInvoked(String testResult) {
        verify(vsTestResultsParser,times(1)).getCoveragePath();
        verify(vsTestResultsParser,times(1)).getTestResultsXmlPath();
        verify(vsTestResultsParser,times(1)).setStdOut(testResult);
        verify(vsTestResultsParser,times(1)).getTestResultsXmlPath();
        verify(vsTestResultsParser,times(1)).getCoveragePath();
    }
    private void assertThatVsTestCommandIsBuilt(File testSettingsFile,
            List<String> unitTestAssemblies, VSTestCommand vsTestCommand) {
        verify(vsTestCommand,times(1)).setUnitTestAssembliesPath(unitTestAssemblies);
        verify(vsTestCommand,times(1)).setTestSettingsFile(testSettingsFile);
        verify(vsTestCommand,times(1)).setCodeCoverage(true);
        verify(vsTestCommand,times(0)).toCommand();
        verify(vsTestCommand,times(0)).toCommandLine();
    }



    private void givenVsTestReturns(String testResult) {
        when(commandLineExecutor.getStdOut()).thenReturn(testResult);
    }
    
    private void givenSolutionDir(File solutionDir) {
        VisualStudioSolution solution = mock(VisualStudioSolution.class);
        when(solution.getSolutionDir()).thenReturn(solutionDir);

        microsoftWindowsEnvironmentMock.givenHasSolution(solution);
    }

    private void givenUnitTestAssemblies(List<String> unitTestAssemblies) {
        assembliesFinderMock.givenFindUnitTestAssembliesFromConfig(unitTestAssemblies);
    }

    private void givenTestSettingsFile(File testSettingsFile) {
        when(testConfigFinder.getTestSettingsFileOrDie(any(File.class),anyString())).thenReturn(testSettingsFile);

    }

    private void expectRunnerIsValid() {
        assertNotNull(runner);
        assertTrue(runner instanceof VsTestUnitTestRunner);
    }

    private void createRunner() {
        DefaultPicoContainer container = new DefaultPicoContainer(new ConstructorInjection());
        container.addComponent(fileSystemMock.getMock())
        .addComponent(testConfigFinder)
        .addComponent(vsTestCommand)
        .addComponent(commandLineExecutor)
        .addComponent(vsTestResultsParser)
        .addComponent(msCoverPropertiesMock.getMock())
        .addComponent(microsoftWindowsEnvironmentMock.getMock())
        .addComponent(assembliesFinderMock.getMock())
        .addComponent(coverageToXmlConverter)
        .addComponent(WindowsCodeCoverageCommandShim.class)
        .addComponent(VsTestRunnerCommandBuilder.class)
        .addComponent(VsTestUnitTestRunner.class)
        .addComponent(testEnvironment);
        runner = container.getComponent(VsTestUnitTestRunner.class);
    }
}
