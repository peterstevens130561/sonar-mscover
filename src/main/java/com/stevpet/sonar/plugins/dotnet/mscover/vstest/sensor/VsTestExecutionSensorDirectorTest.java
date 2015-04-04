package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommandShim;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.CoverageParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.AssembliesFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.InjectingFakesRemoverMock;
import com.stevpet.sonar.plugins.dotnet.mscover.utils.SensorTest;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleanerMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.WindowsVsTestRunner;

public class VsTestExecutionSensorDirectorTest extends SensorTest {

    private VsTestExecutionSensorDirector director = new VsTestExecutionSensorDirector();
    private DefaultPicoContainer container;
    @Before
    public void before() {
        container = super.getContainerWithSensorMocks();
        director.wire(container);

    }
    
    
    @Test
    public void createRunner() {
        //given the container is initialized
        //when I create the unit test runner
        VsTestRunner unitTestRunner = container.getComponent(VsTestRunner.class);
        //then the runner is created and of type WindowsVsTestRunner
        assertNotNull("create WindowsVsTestRunner",unitTestRunner);
        assertTrue("of type WindowsVsTestRunner",unitTestRunner instanceof WindowsVsTestRunner);
    }
    
    @Test
    public void VsTestExecutionSensorDirector_Test() {
        //Given a solution 
        InjectingFakesRemoverMock injectingFakesRemoverMock = new InjectingFakesRemoverMock();
        injectingFakesRemoverMock.replace(container);
        
        TestResultsCleanerMock testResultsCleanerMock = new TestResultsCleanerMock();
        testResultsCleanerMock.replace(container);
        
       
        msCoverPropertiesMock.givenOpenCoverInstallPath("opencover");
        VsTestEnvironment testEnvironment = container.getComponent(VsTestEnvironment.class);
        fileSystemMock.givenWorkDir(new File("bogus/.sonar"));
        testEnvironment.setCoverageXmlPath("bogus/.sonar/coverage.xml");
        VsTestConfigFinderMock vsTestConfigFinderMock = new VsTestConfigFinderMock();
        vsTestConfigFinderMock.givenGetTestSettingsFileOrDie(new File("bogus"));
        vsTestConfigFinderMock.replace(container);
        
        microsoftWindowsEnvironmentMock.givenHasSolutionWithProject(1);
        
        List<String> assemblies=new ArrayList<String>();
        assemblies.add("one");
        assemblies.add("two");
        microsoftWindowsEnvironmentMock.givenHasAssemblies(assemblies );
        
        CommandLineExecutorMock commandLineExecutorMock = new CommandLineExecutorMock();
        container.addComponent(commandLineExecutorMock.getMock());
        container.removeComponent(LockedWindowsCommandLineExecutor.class);
        String stdOut="\nResults File: myfunny.trx\nAttachments:\n  pietje.coverage";
        commandLineExecutorMock.givenGetStdOut(stdOut);
        
        AssembliesFinderMock assembliesFinderMock = new AssembliesFinderMock();
        container.addComponent(assembliesFinderMock.getMock());
        container.removeComponent(DefaultAssembliesFinder.class);
        assembliesFinderMock.onFindUnitTestAssembliesDir("mytestdir");
        CoverageParserMock coverageParserMock = new CoverageParserMock();
        coverageParserMock.replace(container);
        container.removeComponent(OpenCoverCoverageParser.class);

        WindowsCodeCoverageCommandShim windowsCodeCoverageCommandShim=new WindowsCodeCoverageCommandShim();
        container.removeComponent(WindowsCodeCoverageCommand.class);
        container.addComponent(windowsCodeCoverageCommandShim);

        director.execute();
        
        commandLineExecutorMock.thenCommandLine("C:/Program Files (x86)/Microsoft Visual Studio 11.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow/vstest.console.exe /Settings:E:\\Users\\stevpet\\My Documents\\GitHub\\sonar-mscover\\bogus /EnableCodeCoverage /Logger:trx");
        commandLineExecutorMock.thenCommandLine("opencover/OpenCover.Console.Exe -register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*ExcludeFromCodeCoverage* \"-targetdir:mytestdir\" -mergebyhash: \"-output:bogus/.sonar/coverage.xml\" \"-filter:+[one]* +[two]* \"");
        
        coverageParserMock.thenParse("bogus/.sonar/coverage.xml");
        assertEquals("path to test results file","myfunny.trx",testEnvironment.getXmlResultsPath());
        injectingFakesRemoverMock.thenExecuteInvoked();
        testResultsCleanerMock.thenExecuteInvoked();
    }
    
}
