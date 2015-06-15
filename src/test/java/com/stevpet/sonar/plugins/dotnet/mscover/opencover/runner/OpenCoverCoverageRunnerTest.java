package com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.ConstructorInjection;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.VsTestRunnerCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExexutorStub;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ProcessLock;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.VsTestCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.NoAssembliesDefinedException;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.AssembliesFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MicrosoftWindowsEnvironmentMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VSTestStdOutParser;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.VsTestRunnerCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.vstest.WindowsVsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommandMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;


public class OpenCoverCoverageRunnerTest {

    private OpenCoverCoverageRunner openCoverCoverageRunner;
    private OpenCoverCommand  openCoverCommand ;
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VsTestEnvironment testEnvironment;
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private CommandLineExexutorStub commandLineExecutorStub = new CommandLineExexutorStub();
    private FileSystemMock fileSystemMock = new FileSystemMock();
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
        testEnvironment = new VsTestEnvironment(fileSystemMock.getMock());
        testEnvironment.setCoverageXmlPath("coverage.xml");
        assemblies= new ArrayList<String>();
        
        microsoftWindowsEnvironmentMock.givenHasAssemblies(assemblies);

		openCoverCoverageRunner = new OpenCoverCoverageRunner(openCoverCommand, 
                msCoverPropertiesMock.getMock(), 
                testEnvironment, 
                microsoftWindowsEnvironmentMock.getMock(), 
                commandLineExecutorStub,assembliesFinderMock.getMock(), 
                vsTestRunnerCommandBuilderMock.getMock(),
                vsStdOutParserMock.getMock());
        assembliesFinderMock.onFindUnitTestAssembliesDir("somedir");
        vsTestRunnerCommandBuilderMock.givenBuild(vsTestCommandMock.getMock());
        vsTestCommandMock.giveExeDir("exedir");
        vsTestCommandMock.givenArguments("arguments");
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
            openCoverCoverageRunner.execute();
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
        openCoverCoverageRunner.execute();
        //then I expect the proper commandline, with the one assembly
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected =  baseCommandLine + " \"-filter:+[one]* \"";
        assertEquals("building a basic OpenCover commandline for one assembly",expected,commandLine);
    }
    
    @Test
    public void runWithTwoAssemblies() {
        //given one assembly
        assemblies.add("one");
        assemblies.add("two");
        //when I execute the runner
        openCoverCoverageRunner.execute();
        //then I exepct the proper commandline with the two assemblies
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected = baseCommandLine +  " \"-filter:+[one]* +[two]* \"";
        assertEquals("building a basic OpenCover commandline with two assemblies",expected,commandLine);
    }
    
    @Test
    public void instantiateOpenCoverCoverageRunnerThroughIOC() {
        DefaultPicoContainer openCoverContainer = new DefaultPicoContainer(new ConstructorInjection());
        openCoverContainer.addComponent(new ProcessLock("opencover"))
        .addComponent(LockedWindowsCommandLineExecutor.class)
        .addComponent(msCoverPropertiesMock.getMock())
        .addComponent(testEnvironment)
        .addComponent(openCoverCommand)
        .addComponent(microsoftWindowsEnvironmentMock.getMock())
        .addComponent(fileSystemMock.getMock())
        .addComponent(WindowsCodeCoverageCommand.class)
        .addComponent(WindowsVsTestRunner.class)
        .addComponent(VsTestConfigFinder.class)
        .addComponent(OpenCoverCoverageRunner.class)
        .addComponent(VSTestCommand.class)
        .addComponent(DefaultAssembliesFinder.class)
        .addComponent(VSTestStdOutParser.class)
        .addComponent(VsTestCoverageToXmlConverter.class)
        .addComponent(VsTestRunnerCommandBuilder.class);
        OpenCoverCoverageRunner runner = openCoverContainer.getComponent(OpenCoverCoverageRunner.class);
        assertNotNull("creating OpenCoverCoverageRunner through IOC",runner);
        
        TestRunner vsTestRunner = openCoverContainer.getComponent(WindowsVsTestRunner.class);
        assertNotNull(vsTestRunner);
    }
}
