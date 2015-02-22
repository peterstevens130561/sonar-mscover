package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.bootstrap.ProjectBuilder.Context;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.batch.bootstrap.ProjectReactor;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.sonarmocks.SettingsMock;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.VisualStudioSolution;


public class VisualProjectBuilderTest {

    private SettingsMock settingsMock = new SettingsMock();
    private MicrosoftWindowsEnvironment microsoftWindowsEnvironment = new SimpleMicrosoftWindowsEnvironment();
    private VisualStudioProjectBuilder visualProjectBuilder = new VisualStudioProjectBuilder(settingsMock.getMock(),microsoftWindowsEnvironment);
    private ContextMock contextMock = new ContextMock();
    private AssemblyLocatorMock assemblyLocatorMock = new AssemblyLocatorMock();
    
    @Before
    public void setup() {

        ProjectDefinition projectDefinition = ProjectDefinition.create();
        ProjectReactor projectReactor = new ProjectReactor(projectDefinition);
        contextMock.givenProjectReactor(projectReactor);
        
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY,"CodeCoverage.sln");
        File baseDir = TestUtils.getResource("VstoWrapper");
        projectDefinition.setBaseDir(baseDir);
        assemblyLocatorMock.givenLocate("CodeCoverage",new File(baseDir,"CodeCoverage/bin/codecoverage.dll"));
        assemblyLocatorMock.givenLocate("CodeCoverage.UnitTests",new File(baseDir,"CodeCoverage.UnitTests/bin/codecoverage.unittests.dll"));
    }
    
    @Test
    public void ReadSolution_ShouldHAveOneProjectAndOneTestProject() {
        //when
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY,"CodeCoverage.sln");
        visualProjectBuilder.build(contextMock.getMock(), assemblyLocatorMock.getMock());
        
        //check artifactNames
        List<String> artifacts= microsoftWindowsEnvironment.getArtifactNames();
        assertEquals("2 artifacts expected",2,artifacts.size());
        assertTrue("codecoverage.dll to be in artifacts",artifacts.contains("codecoverage.dll"));
        assertTrue("codecoverage.unittests.dll to be in artifacts",artifacts.contains("codecoverage.unittests.dll"));

        //check assemblies
        List<String> assemblies=microsoftWindowsEnvironment.getAssemblies();
        assertEquals("2 assembliess expected",2,assemblies.size());
        assertTrue("CodeCoverage to be in assemblies",assemblies.contains("CodeCoverage"));
        assertTrue("CodeCoverage.UnitTests to be in assemblies",assemblies.contains("CodeCoverage.UnitTests"));
    }
    
    @Test
    public void WrongSolution_ShouldHaveNoSolution() {
        //when
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY,"Bogus.sln");
        visualProjectBuilder.build(contextMock.getMock(), assemblyLocatorMock.getMock());
        
        //check artifactNames
        VisualStudioSolution solution= microsoftWindowsEnvironment.getCurrentSolution();
        assertNull("no solution expected",solution);
    }
    @Test
    public void NosolutionSpecified_ShouldHAveOneProjectAndOneTestProject() {
        //when

        visualProjectBuilder.build(contextMock.getMock(), assemblyLocatorMock.getMock());
        
        //check artifactNames
        List<String> artifacts= microsoftWindowsEnvironment.getArtifactNames();
        assertEquals("2 artifacts expected",2,artifacts.size());
        assertTrue("codecoverage.dll to be in artifacts",artifacts.contains("codecoverage.dll"));
        assertTrue("codecoverage.unittests.dll to be in artifacts",artifacts.contains("codecoverage.unittests.dll"));

        //check assemblies
        List<String> assemblies=microsoftWindowsEnvironment.getAssemblies();
        assertEquals("2 assembliess expected",2,assemblies.size());
        assertTrue("CodeCoverage to be in assemblies",assemblies.contains("CodeCoverage"));
        assertTrue("CodeCoverage.UnitTests to be in assemblies",assemblies.contains("CodeCoverage.UnitTests"));
    }
    @Test
    public void ReadSolution_SkipOneProject() {
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY,"CodeCoverage.sln");
        settingsMock.givenString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS,"CodeCoverage");
        visualProjectBuilder.build(contextMock.getMock(), assemblyLocatorMock.getMock());
        //check artifactNames
        List<String> artifacts= microsoftWindowsEnvironment.getArtifactNames();
        assertEquals("1 artifacts expected",1,artifacts.size());
        assertTrue("codecoverage.unittests.dll to be in artifacts",artifacts.contains("codecoverage.unittests.dll"));

        //check assemblies
        List<String> assemblies=microsoftWindowsEnvironment.getAssemblies();
        assertEquals("1 assembly expected",1,assemblies.size());
        assertTrue("CodeCoverage.UnitTests to be in assemblies",assemblies.contains("CodeCoverage.UnitTests"));
    }
    

}
