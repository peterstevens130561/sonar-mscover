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

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.OpenCoverCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.VsTestRunnerCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.CoverageParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.CoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.runner.OpenCoverCoverageRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.DefaultAssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.TestResultsCleanerMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestConfigFinderMock;

import static org.picocontainer.Characteristics.CACHE;
public class OpenCoverDirectorTest {

    DefaultPicoContainer container;
    private OpenCoverDirector director = new OpenCoverDirector();
    
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private FileSystemMock fileSystemMock = new FileSystemMock();
    
    @Before()
    public void before() {
        container = new DefaultPicoContainer();
        container.addComponent(msCoverPropertiesMock.getMock())
        .as(CACHE).addComponent(VsTestEnvironment.class)
        .addComponent(OpenCoverCommand.class)
        .as(CACHE).addComponent(microsoftWindowsEnvironmentMock.getMock())
        .as(CACHE).addComponent(fileSystemMock.getMock());
        
        director.wire(container);
    }
   
    @Test
    public void OpenCoverDirector_ParserCreation() {
        CoverageParser parser = container.getComponent(CoverageParser.class);
        assertNotNull("create parser",parser);
    }
    
    @Test
    public void OpenCoverDirector_OpenCoverRunnerCreation() {
        CoverageRunner runner = container.getComponent(OpenCoverCoverageRunner.class);
        assertNotNull("create opencover runner",runner);
    }
    
    @Test
    public void OpenCoverDirector_CoverageParserCreation() {
        CoverageParser parser = container.getComponent(CoverageParser.class);
        assertNotNull("create opencover coverage parser",parser);
        assertTrue("should be right class",parser instanceof OpenCoverCoverageParser);
    }
    
    /*
     * Test on a real project
     */
   
    @Test
    public void OpenCoverDirector_Replace() {
        //Given a solution 
        InjectingFakesRemoverMock injectingFakesRemoverMock = new InjectingFakesRemoverMock();
        injectingFakesRemoverMock.replace(container);
        
        TestResultsCleanerMock testResultsCleanerMock = new TestResultsCleanerMock();
        testResultsCleanerMock.replace(container);
        
        msCoverPropertiesMock.givenOpenCoverInstallPath("opencover");
        VsTestEnvironment testEnvironment = container.getComponent(VsTestEnvironment.class);
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
        String stdOut="\nResults File: myfunny.trx";
        commandLineExecutorMock.givenGetStdOut(stdOut);
        
        AssembliesFinderMock assembliesFinderMock = new AssembliesFinderMock();
        container.addComponent(assembliesFinderMock.getMock());
        container.removeComponent(DefaultAssembliesFinder.class);
        assembliesFinderMock.onFindUnitTestAssembliesDir("mytestdir");
        CoverageParserMock coverageParserMock = new CoverageParserMock();
        coverageParserMock.replace(container);
        container.removeComponent(OpenCoverCoverageParser.class);

        director.execute();
        
        commandLineExecutorMock.thenCommandLine("opencover/OpenCover.Console.Exe -register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*ExcludeFromCodeCoverage* \"-targetdir:mytestdir\" -mergebyhash: \"-output:bogus/.sonar/coverage.xml\" \"-filter:+[one]* +[two]* \"");
        coverageParserMock.thenParse("bogus/.sonar/coverage.xml");
        assertEquals("path to test results file","myfunny.trx",testEnvironment.getXmlResultsPath());
        injectingFakesRemoverMock.thenExecuteInvoked();
        testResultsCleanerMock.thenExecuteInvoked();
    }
    
}
