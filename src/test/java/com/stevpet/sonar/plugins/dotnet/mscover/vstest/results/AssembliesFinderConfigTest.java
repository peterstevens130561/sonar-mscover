package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.SolutionHasNoProjectsSonarException;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinder;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.AssembliesFinderFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver.VisualStudioProjectMock;

public class AssembliesFinderConfigTest {

    
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
    private AssembliesFinder assembliesFinder;
    private String buildConfiguration = "Debug";
    private String buildPlatform="x64";
    File testResourceDir;

    @Before()
    public void before() {
        assembliesFinder = new AssembliesFinderFactory().create(msCoverPropertiesMock.getMock());
        testResourceDir=TestUtils.getResource("AssembliesFinderConfigTest");
        
    }
    
    @Test(expected=SolutionHasNoProjectsSonarException.class)
    public void NoProjectList_ExpectSolutionHasNoProjects() {
        List<VisualStudioProject> projects = null;

        assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);  
        fail("expected exception");
    }
    @Test(expected=SolutionHasNoProjectsSonarException.class)
    public void NoProject_ExpectSolutionHasNoProjects() {
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();

        assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);  
        fail("expected exception");
    }
    @Test
    public void NoSpecificSettingsAssemblyOnDefaultPlaceNotUnitTest_AssemblyNotFound() {
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        projects.add(visualStudioProjectMock.getMock());
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        assertNotNull(assemblies);
        assertEquals(0,assemblies.size());    
    }
    
    


    @Test
    public void NoSpecificSettingsAssemblyOnDefaultPlaceIsUnitTest_AssemblyFound() {
        List<VisualStudioProject> projects = givenUnitTestProject();
        String path="solutiondir\\unittest.dll";
        File fut = givenArtifact(buildConfiguration, buildPlatform, path);
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        verifyFound(assemblies, fut);
    }
    
    @Test
    public void notFoundinArtifactButInConfigPlatform_AssemblyFound() {
        List<VisualStudioProject> projects = givenUnitTestProject();

        visualStudioProjectMock.givenDirectory(testResourceDir);
        String bogusPath="solutiondir\\bindebug.dll";
        String expectedPath="\\bin\\Debug\\bindebug.dll";
        File fut = givenArtifact(buildConfiguration, buildPlatform, bogusPath);
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        verifyFound(assemblies, new File(testResourceDir,expectedPath));        
    }
    
    @Test
    public void notFindInArtifactButInHintPath_AssemblyFound() {
        List<VisualStudioProject> projects = givenUnitTestProject();
        String hintPath=new File(testResourceDir,"hintpath").getAbsolutePath().replaceAll("\\\\","/");
        msCoverPropertiesMock.givenUnitTestHintPath(hintPath);
        String bogusPath="solutiondir\\hintpath.dll";
        String expectedPath="hintpath\\hintpath.dll";
        File fut = givenArtifact(buildConfiguration, buildPlatform, bogusPath);
        List<String> assemblies=assembliesFinder.findUnitTestAssembliesFromConfig(null, projects);
        verifyFound(assemblies, new File(testResourceDir,expectedPath));             
    }
    
    @Test
    public void notFoundInArtifactAndAnyWhereElseSpecifiedAsCanBeIgnored_ExpectIgnored() {
        
    }

    private List<VisualStudioProject> givenUnitTestProject() {
        List<VisualStudioProject> projects = new ArrayList<VisualStudioProject>();
        projects.add(visualStudioProjectMock.getMock());
        visualStudioProjectMock.givenIsUnitTest(true);

        msCoverPropertiesMock.givenRequiredBuildPlatform(buildPlatform);
        msCoverPropertiesMock.givenRequiredBuildConfiguration(buildConfiguration);
        return projects;
    }
 
    private File givenArtifact(String buildConfiguration, String buildPlatform,
            String path) {
        assertTrue(testResourceDir.exists());
        File fut=new File(testResourceDir,path);
        visualStudioProjectMock.givenArtifact(buildConfiguration,buildPlatform,fut);
        visualStudioProjectMock.givenArtifactName(fut);
        return fut;
    }

    private void verifyFound(List<String> assemblies, File fut) {
        assertNotNull(assemblies);
        assertEquals(1,assemblies.size()); 
        String found=assemblies.get(0);
        assertEquals(fut.getAbsolutePath(),found);
    }
}
