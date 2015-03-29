package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExexutorStub;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverCoverageRunnerTest {

    private OpenCoverCoverageRunner openCoverCoverageRunner;
    private OpenCoverCommand  openCoverCommand = new OpenCoverCommand() ;
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VsTestEnvironment testEnvironment = new VsTestEnvironment();
    private MicrosoftWindowsEnvironmentMock microsoftWindowsEnvironmentMock = new MicrosoftWindowsEnvironmentMock();
    private CommandLineExexutorStub commandLineExecutorStub = new CommandLineExexutorStub();
    
    private List<String> assemblies;
    
    @Before
    public void before() {
        msCoverPropertiesMock.givenOpenCoverInstallPath("opencover");
        testEnvironment.setCoverageXmlPath("coverage.xml");
        assemblies= new ArrayList<String>();
        
        microsoftWindowsEnvironmentMock.givenHasAssemblies(assemblies);
        openCoverCoverageRunner = new OpenCoverCoverageRunner(openCoverCommand, 
                msCoverPropertiesMock.getMock(), 
                testEnvironment, 
                microsoftWindowsEnvironmentMock.getMock(), 
                commandLineExecutorStub);
    }
    
    /**
     * Scenario: no assemblies specified, expect exception
     */
    @Test
    public void runWithNoAssembly() {
        //given no assembly

        //when
        openCoverCoverageRunner.execute();
        //then an exception should be thrown
        String commandLine=commandLineExecutorStub.getCommandLine();
        try {
        String expected = "opencover/OpenCover.Console.Exe -register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*ExcludeFromCodeCoverage* -mergebyhash: \"-output:coverage.xml\" \"-filter:+[one]* \"";
        } catch (OpenCoverRunnerNoAssembliesDefinedException e) {
            return;
        }
        fail("no assemblies specified, expected exception");
    }

    @Test
    public void runWithOneAssembly() {
        //given one assembly
        assemblies.add("one");

        //when
        openCoverCoverageRunner.execute();
        //then
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected = "opencover/OpenCover.Console.Exe -register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*ExcludeFromCodeCoverage* -mergebyhash: \"-output:coverage.xml\" \"-filter:+[one]* \"";
        assertEquals("building a basic OpenCover commandline",expected,commandLine);
    }
    
    @Test
    public void runWithTwoAssemblies() {
        //given one assembly
        assemblies.add("one");
        assemblies.add("two");
        //when
        openCoverCoverageRunner.execute();
        //then
        String commandLine=commandLineExecutorStub.getCommandLine();
        String expected = "opencover/OpenCover.Console.Exe -register:user -excludebyfile:*\\*.Designer.cs -excludebyattribute:*ExcludeFromCodeCoverage* -mergebyhash: \"-output:coverage.xml\" \"-filter:+[one]* +[two]* \"";
        assertEquals("building a basic OpenCover commandline",expected,commandLine);
    }
}
