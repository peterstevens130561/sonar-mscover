package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.DefaultCoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.CoverageReaderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.DefaultResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver.ResourceResolver;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.utils.AbstractSensorTest;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.command.VSTestCommandMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleanerMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.DefaultDirector;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.OpenCoverWorkflowSteps;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.UnitTestBatchData;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.WorkflowDirector;

import static org.mockito.Mockito.mock;
public class OpenCoverWorkflowTest extends AbstractSensorTest {

    DefaultPicoContainer container;
    private OpenCoverWorkflowSteps steps = new OpenCoverWorkflowSteps();
    UnitTestBatchData data = new UnitTestBatchData();
    private WorkflowDirector director = new DefaultDirector(data);
    
    
    @Before()
    public void before() {
        container = super.getContainerWithSensorMocks();
        container.addComponent(steps);
        director.wire(container);
    }
   
    @Test
    public void OpenCoverDirector_ParserCreation() {
        CoverageReader reader = container.getComponent(CoverageReader.class);
        assertNotNull("create parser",reader);
    }
    
    @Test
    public void OpenCoverDirector_OpenCoverRunnerCreation() {
        TestRunner runner = container.getComponent(OpenCoverCoverageRunner.class);
        assertNotNull("create opencover runner",runner);
    }
    
    @Test
    public void OpenCoverDirector_CoverageParserCreation() {
        CoverageReader parser = container.getComponent(CoverageReader.class);
        assertNotNull("create opencover coverage parser",parser);
        assertTrue("should be right class",parser instanceof DefaultCoverageReader);
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
        microsoftWindowsEnvironmentMock.givenHasUnitTestSourceFiles(true);
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
        CoverageReaderMock coverageParserMock = new CoverageReaderMock();
        coverageParserMock.replace(container);
        container.removeComponent(DefaultCoverageReader.class);

        VSTestCommandMock vsTestCommandMock = new VSTestCommandMock();
        vsTestCommandMock.giveExeDir("vstest.console.exe");
        vsTestCommandMock.givenArguments("arguments");
        container.removeComponent(VSTestCommand.class);
        container.addComponent(vsTestCommandMock.getMock());
        
        container.removeComponent(DefaultResourceResolver.class);
        container.addComponent(mock(DefaultResourceResolver.class));
        //SensorContextMock sensorContextMock = new SensorContextMock();
		//container.addComponent(sensorContextMock.getMock());
        director.execute();
        
        String coveragePath=coverageFile.getAbsolutePath().replaceAll("\\\\","/");
		commandLineExecutorMock.thenCommandLine("opencover/OpenCover.Console.Exe -register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*ExcludeFromCodeCoverage* " +
        		"\"-target:vstest.console.exe\" \"-targetdir:mytestdir\" -mergebyhash: " + 
        		"\"-targetargs:arguments\" \"-output:" + coveragePath  + "\" \"-filter:+[one]* +[two]* \"");
        coverageParserMock.thenParse(coveragePath);
        //only interested in the name, as the absolute path makes it non runnable on other environments


    }
    
}
