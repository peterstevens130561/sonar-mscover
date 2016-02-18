package com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.VsTestRunnerCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExexutorStub;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssembliesDefinedException;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.AssembliesFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommandMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DefaultOpenCoverTestRunnerTest {

    private DefaultOpenCoverTestRunner openCoverCoverageRunner;
    private OpenCoverCommand  openCoverCommand ;
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VsTestEnvironment testEnvironment;
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private CommandLineExexutorStub commandLineExecutorStub = new CommandLineExexutorStub();
    private AssembliesFinderMock assembliesFinderMock = new AssembliesFinderMock();
    private List<String> assemblies;
	private VsTestRunnerCommandBuilderMock vsTestRunnerCommandBuilderMock = new VsTestRunnerCommandBuilderMock();
	private VSTestStdOutParserMock vsStdOutParserMock= new VSTestStdOutParserMock();
	private VSTestCommandMock vsTestCommandMock = new VSTestCommandMock();
	private String baseCommandLine="opencover/OpenCover.Console.Exe " +
			"-register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*ExcludeFromCodeCoverage* " +
			"\"-target:exedir\" \"-targetdir:somedir\" -mergebyhash: \"-targetargs:arguments\" \"-output:coverage.xml\"";
    
    /**
     * Setting up the mocks/stubs to work with. Note that it is assumed the builder does its work.
     */
    @Before
    public void before() {
        msCoverPropertiesMock.givenOpenCoverInstallPath("opencover");
        openCoverCommand = new OpenCoverCommand(msCoverPropertiesMock.getMock());
        testEnvironment = new VsTestEnvironment();
        testEnvironment.setCoverageXmlPath("coverage.xml");
        assemblies= new ArrayList<String>();
        
        microsoftWindowsEnvironmentMock.givenHasAssemblies(assemblies);

		openCoverCoverageRunner = new DefaultOpenCoverTestRunner(msCoverPropertiesMock.getMock(),microsoftWindowsEnvironmentMock.getMock(),openCoverCommand,assembliesFinderMock.getMock(),
				vsTestRunnerCommandBuilderMock.getMock(),vsStdOutParserMock.getMock(),commandLineExecutorStub,testEnvironment);
        assembliesFinderMock.onFindUnitTestAssembliesDir("somedir");
        vsTestRunnerCommandBuilderMock.givenBuild(vsTestCommandMock.getMock());
        vsTestCommandMock.giveExeDir("exedir");
        vsTestCommandMock.givenArguments("arguments");
        Pattern pattern = Pattern.compile("something to meet the need");
        openCoverCoverageRunner.setTestProjectPattern(pattern);
    }
    
    /**
     * Scenario: no assemblies specified, expect exception
     */
    @Test
    public void runWithNoAssembly() {
        //given no assembly
        String commandLine;
        try {
            //when I execute the runner
            openCoverCoverageRunner.onlyReportAssembliesOfTheSolution().execute();
            commandLine=commandLineExecutorStub.getCommandLine();
        } catch (NoAssembliesDefinedException e) {
            // then the NoAssembliesDefinedException should be thrown
            return;
        }
        fail("no assemblies specified, expected exception, but got " + commandLine);
    }



    @Test
    public void runWithOneAssembly_CommandLineOk() {
        //given one assembly
        assemblies.add("one");
        //when
        openCoverCoverageRunner.onlyReportAssembliesOfTheSolution().execute();
        //then I expect the proper commandline, with the one assembly
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected =  baseCommandLine + " \"-filter:+[one]* \"";
        assertEquals("building a basic OpenCover commandline for one assembly",expected,commandLine);
    }
    
    @Test
    public void runWithOneNotIncludedAssembly_CommandLineOk() {
        //given one assembly
        assemblies.add("one");
        //when
        openCoverCoverageRunner.execute();
        //then I expect the proper commandline, with the one assembly
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected =  baseCommandLine;
        assertEquals("building a basic OpenCover commandline for one assembly",expected,commandLine);
    }
    @Test
    public void runWithTwoAssemblies() {
        //given one assembly
        assemblies.add("one");
        assemblies.add("two");
        //when I execute the runner
        openCoverCoverageRunner.onlyReportAssembliesOfTheSolution().execute();
        //then I exepct the proper commandline with the two assemblies
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected = baseCommandLine +  " \"-filter:+[one]* +[two]* \"";
        assertEquals("building a basic OpenCover commandline with two assemblies",expected,commandLine);
    }
    
    
    @Test
    public void checkPattern() {
        Pattern bogusPattern = Pattern.compile("bogus");
        assemblies.add("one");
        openCoverCoverageRunner.setTestProjectPattern(bogusPattern);
        openCoverCoverageRunner.execute();
        VsTestRunnerCommandBuilder vsTestRunner=vsTestRunnerCommandBuilderMock.getMock();
        verify(vsTestRunner,times(1)).setTestProjectPattern(bogusPattern); 
    }
}
