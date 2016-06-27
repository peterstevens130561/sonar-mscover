package com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.stevpet.sonar.plugins.dotnet.mscover.VsTestRunnerCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExexutorStub;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssembliesDefinedException;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.AssembliesFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.DefaultOpenCoverTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverCommandLineConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommandMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParserMock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultOpenCoverTestRunnerTest {

    private static final int TIMEOUT_SET = 45;
    private DefaultOpenCoverTestRunner openCoverCoverageRunner;
    private OpenCoverCommand  openCoverCommand ;
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private CommandLineExexutorStub commandLineExecutorStub = new CommandLineExexutorStub();
    private AssembliesFinderMock assembliesFinderMock = new AssembliesFinderMock();
    private List<String> assemblies;
	private VsTestRunnerCommandBuilderMock vsTestRunnerCommandBuilderMock = new VsTestRunnerCommandBuilderMock();
	private VSTestStdOutParserMock vsStdOutParserMock= new VSTestStdOutParserMock();
	private VSTestCommandMock vsTestCommandMock = new VSTestCommandMock();
	private String baseCommandLine="opencover/OpenCover.Console.Exe " +
			"-register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*.ExcludeFromCoverageAttribute* " +
			"\"-target:exedir\" \"-targetdir:somedir\" -mergebyhash: \"-targetargs:arguments\" -hideskipped:all \"-output:";
    private String coveragePath;
    private @Mock OpenCoverCommandLineConfiguration configuration;
    
    /**
     * Setting up the mocks/stubs to work with. Note that it is assumed the builder does its work.
     */
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        openCoverCommand = new OpenCoverCommand();
        assemblies= new ArrayList<String>();
        
        microsoftWindowsEnvironmentMock.givenHasAssemblies(assemblies);

		openCoverCoverageRunner = new DefaultOpenCoverTestRunner(configuration, microsoftWindowsEnvironmentMock.getMock(),openCoverCommand,assembliesFinderMock.getMock(),
				vsTestRunnerCommandBuilderMock.getMock(),vsStdOutParserMock.getMock(),commandLineExecutorStub);
		when(configuration.getInstallDir()).thenReturn("opencover");
		when(configuration.getRegister()).thenReturn("user");
		when(configuration.getSkipAutoProps()).thenReturn(false);
        assembliesFinderMock.onFindUnitTestAssembliesDir("somedir");
        vsTestRunnerCommandBuilderMock.givenBuild(vsTestCommandMock.getMock());
        vsTestCommandMock.giveExeDir("exedir");
        vsTestCommandMock.givenArguments("arguments");
        Pattern pattern = Pattern.compile("something to meet the need");
        openCoverCoverageRunner.setTestProjectPattern(pattern);
        File coverageFile=new File("coverage.xml");
        coveragePath=coverageFile.getAbsolutePath().replaceAll("\\\\", "/") + "\"";
        openCoverCoverageRunner.setCoverageFile(coverageFile);
    }
    
    /**
     * Scenario: no assemblies specified, expect exception
     */
    @Test
    public void runWithNoAssembly() {
        //given no assembly

        try {
            //when I execute the runner
            openCoverCoverageRunner.onlyReportAssembliesOfTheSolution().execute();
        } catch (NoAssembliesDefinedException e) {
            // then the NoAssembliesDefinedException should be thrown
            return;
        }
        fail("no assemblies specified, expected exception");
    }



    @Test
    public void runWithOneAssembly_CommandLineOk() {
        minimalSetup();
        //then I expect the proper commandline, with the one assembly
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected =  baseCommandLine + coveragePath + " \"-filter:+[one]* \"";
        compareCommandLines("building a basic OpenCover commandline for one assembly",expected,commandLine);

    }
    
    @Test
    public void runWithOneNotIncludedAssembly_CommandLineOk() {
        //given one assembly
        assemblies.add("one");
        //when
        openCoverCoverageRunner.execute();
        //then I expect the proper commandline, with the one assembly
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected =  baseCommandLine + coveragePath;
        compareCommandLines("building a basic OpenCover commandline for one assembly",expected,commandLine);
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
        String expected = baseCommandLine +  coveragePath + " \"-filter:+[one]* +[two]* \"";
        compareCommandLines("building a basic OpenCover commandline with two assemblies",expected,commandLine);
    }
    
    private void compareCommandLines(String msg,String expected,String actual) {
        if(expected.split(" ").length != actual.split(" ").length) {
            assertEquals("different number of arguments",expected,actual);
        }
        for(String argument : expected.split(" ")) {
            assertTrue(msg + "expected "+ argument,actual.contains(argument));
        }
        
        
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
    
    @Test
    public void checkTimeoutDefault() {
        minimalSetup();
        //then I expect the proper commandline, with the one assembly
        openCoverCoverageRunner.execute();
        assertEquals("timeout default",30,commandLineExecutorStub.getTimeoutMinutes());
    }
    
    @Test
    public void checkTimeoutSet() {
        minimalSetup();
        //then I expect the proper commandline, with the one assembly
        openCoverCoverageRunner.setTimeout(TIMEOUT_SET);
        openCoverCoverageRunner.execute();
        assertEquals("timeout should be set",TIMEOUT_SET,commandLineExecutorStub.getTimeoutMinutes());
    }
    
    @Test
    public void checkSkipAutoProps() {
        minimalSetup();
        
        when(configuration.getSkipAutoProps()).thenReturn(true);
        openCoverCoverageRunner.execute();
        String commandLine=openCoverCommand.toCommandLine();
        assertTrue("should contain the skipAutoProps argument",commandLine.contains(" -skipautoprops"));
    }
    
    @Test
    public void checkNoSkipAutoProps() {
        minimalSetup();
        
        when(configuration.getSkipAutoProps()).thenReturn(false);
        openCoverCoverageRunner.execute();
        String commandLine=openCoverCommand.toCommandLine();
        assertFalse("should not contain the skipAutoProps argument",commandLine.contains(" -skipautoprops"));
    }

    private void minimalSetup() {
        assemblies.add("one");
        openCoverCoverageRunner.onlyReportAssembliesOfTheSolution().execute();
    }
    
}
