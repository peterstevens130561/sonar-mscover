package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.VisualStudioSolutionMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.VisualStudioProjectMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;


public class OpenCoverCommandBuilderBuildTest {

    private OpenCoverCommandMock openCoverCommandMock = new OpenCoverCommandMock();
    private VisualStudioSolutionMock visualStudioSolutionMock = new VisualStudioSolutionMock();
    private VsTestRunnerMock vsTestRunnerMock = new VsTestRunnerMock();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    
    @Test()
    public void NoAssembly() {
        OpenCoverCommandBuilder builder = givenPreparedBuilder();
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("");
    }

    private OpenCoverCommandBuilder givenPreparedBuilder() {
        OpenCoverCommandBuilder builder = new OpenCoverCommandBuilder();
        builder.setOpenCoverCommand(openCoverCommandMock);
        builder.setSolution(visualStudioSolutionMock);
        builder.setTestRunner(vsTestRunnerMock);
        builder.setMsCoverProperties(msCoverPropertiesMock);
        builder.setTestEnvironment(vsTestEnvironmentMock);
        vsTestEnvironmentMock.givenXmlCoveragePath("somepath");
        return builder;
    }
    
    @Test
    public void OneAssembly_ExpectInFilter() {
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
