package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.VisualStudioSolutionMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.VsTestRunner;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.VisualStudioProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;


public class OpenCoverCommandBuilderBuildTest {

    private OpenCoverCommandMock openCoverCommandMock = new OpenCoverCommandMock();
    private OpenCoverCommand openCoverCommand;
    private VisualStudioSolutionMock visualStudioSolutionMock = new VisualStudioSolutionMock();
    private VisualStudioSolution solution;
    private VsTestRunnerMock vsTestRunnerMock = new VsTestRunnerMock();
    private VsTestRunner unitTestRunner;
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private MsCoverProperties msCoverProperties;
    private VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    private VsTestEnvironment vsTestEnvironment;
    
    @Before
    public void before() {
        openCoverCommand = openCoverCommandMock.getMock();
        solution=visualStudioSolutionMock.getMock();
        unitTestRunner = vsTestRunnerMock.getMock();
        msCoverProperties=msCoverPropertiesMock.getMock();
        vsTestEnvironment=vsTestEnvironmentMock.getMock();
    }
    
    @Test()
    public void NoAssembly() {
        OpenCoverCommandBuilder builder = givenPreparedBuilder();
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("");
    }

    private OpenCoverCommandBuilder givenPreparedBuilder() {
        OpenCoverCommandBuilder builder = new OpenCoverCommandBuilder();
        builder.setOpenCoverCommand(openCoverCommand);
        builder.setSolution(solution);
        builder.setTestRunner(unitTestRunner);
        builder.setMsCoverProperties(msCoverProperties);
        builder.setTestEnvironment(vsTestEnvironment);
        vsTestEnvironmentMock.givenXmlCoveragePath("somepath");
        return builder;
    }
    
    @Test
    public void OneAssembly() {
        givenProject("assembly");
        OpenCoverCommandBuilder builder = givenPreparedBuilder();
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("+[assembly]* ");
    }
    
    @Test
    public void TwoAssemblies_ExpectInFilter() {
        givenProject("assembly1");
        givenProject("assembly2");
        OpenCoverCommandBuilder builder = givenPreparedBuilder();
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("+[assembly1]* +[assembly2]* ");
    }

    private void givenProject(String name) {
        VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
        visualStudioProjectMock.givenAssemblyName(name);
        visualStudioSolutionMock.givenProjectMock(visualStudioProjectMock);
    }
    

}
