package com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.picocontainer.DefaultPicoContainer;

import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.WindowsCodeCoverageCommandShim;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.CoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.VsTestCoverageToXmlConverter;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.CoverageReaderMock;
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
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.TestRunner;

public class VsTestExecutionSensorDirectorTest extends SensorTest {

    private VsTestExecutionSensorDirector director = new VsTestExecutionSensorDirector();
    private DefaultPicoContainer container;
	private InjectingFakesRemoverMock injectingFakesRemoverMock;
	private TestResultsCleanerMock testResultsCleanerMock;
	private VsTestConfigFinderMock vsTestConfigFinderMock;
	private CommandLineExecutorMock commandLineExecutorMock;
	private AssembliesFinderMock assembliesFinderMock;
	private CoverageReaderMock coverageParserMock;
	private WindowsCodeCoverageCommandShim windowsCodeCoverageCommandShim;
	private CoverageToXmlConverter coverageToXmlConverter;
    @Before
    public void before() {
    	MockitoAnnotations.initMocks(this);
        container = super.getContainerWithSensorMocks();
        director.wire(container);

    }
    
    
    @Test
    public void createRunner() {
        //given the container is initialized
        //when I create the unit test runner
        TestRunner unitTestRunner = container.getComponent(TestRunner.class);
        //then the runner is created and of type WindowsVsTestRunner
        assertNotNull("could not create WindowsVsTestRunner",unitTestRunner);
        assertTrue("of type WindowsVsTestRunner",unitTestRunner instanceof WindowsVsTestRunner);
    }
    
    @Test
    public void VsTestExecutionSensorDirector_Test() {
        createMocks();
        VsTestEnvironment testEnvironment = container.getComponent(VsTestEnvironment.class);
        //given workdir is "bogus/.sonar"
        File workDir=new File("\\bogus/.sonar");
        fileSystemMock.givenWorkDir(workDir);
        testEnvironment.setCoverageXmlPath("bogus/.sonar/coverage.xml");
        //given testsettings file is "bogus"
        vsTestConfigFinderMock.givenGetTestSettingsFileOrDie(new File("C:\\bogus"));
        
        //given a solution with one project
        microsoftWindowsEnvironmentMock.givenHasSolutionWithProject(1);
        
        //given assemblies "one,two"
        List<String> assemblies=new ArrayList<String>();
        assemblies.add("one");
        assemblies.add("two");
        microsoftWindowsEnvironmentMock.givenHasAssemblies(assemblies );
        
        //given results file is "myfunny.trx" and coverage file is "pietje.coverage"
        String stdOut="\nResults File: myfunny.trx\nAttachments:\n  pietje.coverage";
        commandLineExecutorMock.givenGetStdOut(stdOut);
        
        assembliesFinderMock.onFindUnitTestAssembliesDir("mytestdir");

        // when analysed
        director.execute();
        
        //then vstest.console.exe is invoked
        commandLineExecutorMock.thenCommandLine("C:/Program Files (x86)/Microsoft Visual Studio 11.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow/vstest.console.exe " +
        "/Settings:C:\\bogus /EnableCodeCoverage /Logger:trx");
        
        // and then codecoverage is invoked
        String prefix = workDir.getAbsolutePath();
        File coverageFile = new File("pietje.coverage");
        File outputFile = new File("bogus/.sonar/coverage.xml");
        String expectedCommandLine=prefix + "\\CodeCoverage\\CodeCoverage.exe \"" + coverageFile.getAbsolutePath() + "\" \"" +
        		outputFile.getAbsolutePath() + "\"";
        commandLineExecutorMock.thenCommandLine(expectedCommandLine);
        // and then name of test results file is 
        assertEquals("path to test results file","myfunny.trx",new File(testEnvironment.getXmlResultsPath()).getName());
        injectingFakesRemoverMock.thenExecuteInvoked();
        testResultsCleanerMock.thenExecuteInvoked();
    }


	private void createMocks() {
		injectingFakesRemoverMock = new InjectingFakesRemoverMock();
        injectingFakesRemoverMock.replace(container);
        
        testResultsCleanerMock = new TestResultsCleanerMock();
        testResultsCleanerMock.replace(container);
        
        msCoverPropertiesMock.givenOpenCoverInstallPath("opencover");

        
        vsTestConfigFinderMock = new VsTestConfigFinderMock();
        vsTestConfigFinderMock.replace(container);
        
        commandLineExecutorMock = new CommandLineExecutorMock();
        container.addComponent(commandLineExecutorMock.getMock());
        container.removeComponent(LockedWindowsCommandLineExecutor.class);
        
        assembliesFinderMock = new AssembliesFinderMock();
        container.addComponent(assembliesFinderMock.getMock());
        container.removeComponent(DefaultAssembliesFinder.class);
        
        coverageParserMock = new CoverageReaderMock();
        coverageParserMock.replace(container);
        container.removeComponent(DefaultCoverageReader.class);
        
        windowsCodeCoverageCommandShim = new WindowsCodeCoverageCommandShim();
        container.removeComponent(WindowsCodeCoverageCommand.class);
        container.addComponent(windowsCodeCoverageCommandShim);
	}
    
}
