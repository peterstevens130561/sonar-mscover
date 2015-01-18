package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.OpenCoverCommandBuilderMock;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutorMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.OpenCoverParserFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.ProjectSeamMock;
import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.FileSystemMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VSTestStdOutParserMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunnerFactoryMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;

public class OpenCoverTestExecutionCoverageSensorBehavior {
    OpenCoverTestExecutionCoverageSensor sensor;
    private MsCoverProperties msCoverProperties;
    private VsTestEnvironmentMock vsTestEnvironment = new VsTestEnvironmentMock();
    private FileSystem fileSystem = mock(FileSystem.class);
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment ;
    private Project project = mock(Project.class);
    private SensorContext context = mock(SensorContext.class);

    
    public void setModuleFileSystem(FileSystemMock fileSystemMock ) {
        fileSystem = fileSystemMock.getMock();
    }

    public void givenANewSensor() {
        sensor=new OpenCoverTestExecutionCoverageSensor(msCoverProperties, 
                microsoftWindowsEnvironment, 
                fileSystem, 
                vsTestEnvironment.getMock());
    }

    public void verifyThatSensorExists() {
        assertNotNull(sensor);
    }
    
    public VsTestEnvironmentMock getVsTestEnvironmentMock() {
        return vsTestEnvironment;
    }

    public void verifyShouldAnalyseReturns(boolean expected) {
        boolean result=sensor.shouldExecuteOnProject(project);
        assertEquals(expected,result);
    }

    public void setProject(Project project) {
        this.project=project;
    }

    public void setMsCoverProperties(MsCoverProperties msCoverProperties) {
        this.msCoverProperties=msCoverProperties;
    }

    public void setMicrosoftWindowsEnvironment(MicrosoftWindowsEnvironment microsoftWindowsEnvironment) {
        this.microsoftWindowsEnvironment=microsoftWindowsEnvironment;
    }

    public void setOpenCoverCommand(OpenCoverCommand mock) {
        sensor.setOpenCoverCommand(mock);
    }

    public void analyse() {
        sensor.analyse(project, context);      
    }

    public void givenProjectSeam(ProjectSeamMock projectSeamMock) {
        sensor.setProjectSeam(projectSeamMock.getMock());
    }

    public void givenOpenCoverCommandBuilder(
            OpenCoverCommandBuilderMock openCoverCommandBuilderMock) {
        sensor.setOpenCoverCommandBuilder(openCoverCommandBuilderMock.getMock());
    }

    public void givenAssembliesFinderFactory(
            AssembliesFinderFactoryMock assembliesFinderFactoryMock) {
        sensor.setAssembliesFinderFactory(assembliesFinderFactoryMock.getMock());
    }

    public void givenCommandLineExecutor(
            CommandLineExecutorMock commandLineExecutorMock) {
        sensor.setCommandLineExecutor(commandLineExecutorMock.getMock());
    }

    public void givenVsTestStdOutParser(VSTestStdOutParserMock vsTestStdOutParserMock) {
        sensor.setVsTestStdOutParser(vsTestStdOutParserMock.getMock());
    }

    public void givenOpenCoverParserFactory(
            OpenCoverParserFactoryMock openCoverParserFactoryMock) {
        sensor.setOpenCoverParserFactory(openCoverParserFactoryMock.getMock());
    }

    public void givenTestRunnerFactory(
            VsTestRunnerFactoryMock vsTestRunnerFactoryMock) {
        sensor.setVsTestRunnerFactory(vsTestRunnerFactoryMock.getMock());
    }

    public void givenFakesRemover(FakesRemoverMock fakesRemoverMock) {
        sensor.setFakesRemover(fakesRemoverMock.getMock());
    };
}
