package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.SensorContextMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.CoverageParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.CoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.saver.test.MeasureSaverMock;
import com.stevpet.sonar.plugins.dotnet.mscover.utils.SensorTest;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommandMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleanerMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.OpenCoverWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;

import static org.mockito.Mockito.mock;
public class OpenCoverWorkflowTest extends SensorTest {

    DefaultPicoContainer container;
    private OpenCoverWorkflowSteps steps = new OpenCoverWorkflowSteps();
    private WorkflowDirector director = new DefaultDirector(steps);
    
    
    @Before()
    public void before() {
        container = super.getContainerWithSensorMocks();
        director.wire(container);
    }
   
    @Test
    public void OpenCoverDirector_ParserCreation() {
        CoverageParserStep parser = container.getComponent(CoverageParserStep.class);
        assertNotNull("create parser",parser);
    }
    
    @Test
    public void OpenCoverDirector_OpenCoverRunnerCreation() {
        CoverageRunner runner = container.getComponent(OpenCoverCoverageRunner.class);
        assertNotNull("create opencover runner",runner);
    }
    
    @Test
    public void OpenCoverDirector_CoverageParserCreation() {
        CoverageParserStep parser = container.getComponent(CoverageParserStep.class);
        assertNotNull("create opencover coverage parser",parser);
        assertTrue("should be right class",parser instanceof OpenCoverCoverageParser);
    }
    
    /*
     * Test on a real project
     */
   
    @Test
    public void OpenCoverWorkflow_SociableTest() {
        //Given a solution 
        InjectingFakesRemoverMock injectingFakesRemoverMock = new InjectingFakesRemoverMock();
        injectingFakesRemoverMock.replace(container);
        
        TestResultsCleanerMock testResultsCleanerMock = new TestResultsCleanerMock();
        testResultsCleanerMock.replace(container);
        
        msCoverPropertiesMock.givenOpenCoverInstallPath("opencover");
        VsTestEnvironment testEnvironment = container.getComponent(VsTestEnvironment.class);
        File coverageFile = TestUtils.getResource("coverage-report.xml");
        testEnvironment.setCoverageXmlPath(coverageFile.getAbsolutePath());
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
        File resultsFile = TestUtils.getResource("results.trx");
        String stdOut="\nResults File: " + resultsFile.getAbsolutePath();
        commandLineExecutorMock.givenGetStdOut(stdOut);
        
        AssembliesFinderMock assembliesFinderMock = new AssembliesFinderMock();
        container.addComponent(assembliesFinderMock.getMock());
        container.removeComponent(DefaultAssembliesFinder.class);
        assembliesFinderMock.onFindUnitTestAssembliesDir("mytestdir");
        CoverageParserMock coverageParserMock = new CoverageParserMock();
        coverageParserMock.replace(container);
        container.removeComponent(OpenCoverCoverageParser.class);

        VSTestCommandMock vsTestCommandMock = new VSTestCommandMock();
        vsTestCommandMock.giveExeDir("vstest.console.exe");
        vsTestCommandMock.givenArguments("arguments");
        container.removeComponent(VSTestCommand.class);
        container.addComponent(vsTestCommandMock.getMock());
        
        container.removeComponent(DefaultResourceResolver.class);
        container.addComponent(mock(ResourceResolver.class));
        SensorContextMock sensorContextMock = new SensorContextMock();
		container.addComponent(sensorContextMock.getMock());
		container.addComponent(new MeasureSaverMock().getMock());
        director.execute();
        
        String coveragePath=coverageFile.getAbsolutePath().replaceAll("\\\\","/");
		commandLineExecutorMock.thenCommandLine("opencover/OpenCover.Console.Exe -register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*ExcludeFromCodeCoverage* " +
        		"\"-target:vstest.console.exe\" \"-targetdir:mytestdir\" -mergebyhash: " + 
        		"\"-targetargs:arguments\" \"-output:" + coveragePath  + "\" \"-filter:+[one]* +[two]* \"");
        coverageParserMock.thenParse(coveragePath);
        //only interested in the name, as the absolute path makes it non runnable on other environments
        String resultsPath=testEnvironment.getXmlResultsPath();
        assertNotNull("test results file not set",resultsPath);
        File actualResultsFile = new File(resultsPath);
        assertEquals("path to test results file",resultsFile.getName(),actualResultsFile.getName());
        //injectingFakesRemoverMock.thenExecuteInvoked();
        //testResultsCleanerMock.thenExecuteInvoked();
    }
    
}
