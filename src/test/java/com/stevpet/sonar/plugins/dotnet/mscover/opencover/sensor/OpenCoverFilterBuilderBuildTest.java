package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.mock.VisualStudioSolutionMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.VisualStudioProjectMock;

public class OpenCoverFilterBuilderBuildTest {

    private OpenCoverCommandMock openCoverCommandMock = new OpenCoverCommandMock();
    private OpenCoverCommand openCoverCommand;
    private VisualStudioSolutionMock visualStudioSolutionMock = new VisualStudioSolutionMock();
    private VisualStudioSolution solution;
    
    @Before
    public void before() {
        openCoverCommand = openCoverCommandMock.getMock();
        solution=visualStudioSolutionMock.getMock();
    }
    
    @Test
    public void NoAssembly() {
        OpenCoverFilterBuilder builder = new OpenCoverFilterBuilder();
        builder.setOpenCoverCommand(openCoverCommand);
        builder.setSolution(solution);
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("");
    }
    
    @Test
    public void OneAssembly() {
        givenProject("assembly");
        OpenCoverFilterBuilder builder = new OpenCoverFilterBuilder();
        builder.setOpenCoverCommand(openCoverCommand);
        builder.setSolution(solution);
        builder.build();
        openCoverCommandMock.verifySetExcludeFromCodeCoverageAttributeFilter();
        openCoverCommandMock.verifyFilter("+[assembly]* ");
    }
    
    @Test
    public void TwoAssemblies_ExpectInFilter() {
        givenProject("assembly1");
        givenProject("assembly2");
        OpenCoverFilterBuilder builder = new OpenCoverFilterBuilder();
        builder.setOpenCoverCommand(openCoverCommand);
        builder.setSolution(solution);
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
