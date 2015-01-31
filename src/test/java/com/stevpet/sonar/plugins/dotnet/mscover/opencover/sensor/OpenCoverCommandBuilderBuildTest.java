package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.sensor.VsTestEnvironmentMock;


public class OpenCoverCommandBuilderBuildTest {

    private OpenCoverCommandMock openCoverCommandMock = new OpenCoverCommandMock();
    private VsTestRunnerMock vsTestRunnerMock = new VsTestRunnerMock();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VsTestEnvironmentMock vsTestEnvironmentMock = new VsTestEnvironmentMock();
    private List<String> assemblies;
    
    @Before() 
    public void before(){
        assemblies=new ArrayList<String>();
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
        builder.setOpenCoverCommand(openCoverCommandMock.getMock());
        builder.setAssemblies(assemblies);
        builder.setTestRunner(vsTestRunnerMock.getMock());
        builder.setMsCoverProperties(msCoverPropertiesMock.getMock());
        builder.setTestEnvironment(vsTestEnvironmentMock.getMock());
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
        assemblies.add(name);
    }
    

}
