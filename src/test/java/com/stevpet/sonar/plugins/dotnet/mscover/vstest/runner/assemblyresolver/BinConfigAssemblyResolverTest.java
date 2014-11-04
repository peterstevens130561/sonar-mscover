package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;


import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;

public class BinConfigAssemblyResolverTest {
    private AssemblyResolverTestUtils utils = new AssemblyResolverTestUtils();
    private MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    private String artifactName = "somename.dll";
    VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
    
    private AssemblyResolver assemblyResolver = new BinConfigAssemblyResolver();
    ProjectMock projectMock = new ProjectMock();
    
    @Before() 
    public void before() {
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        utils.setAssemblyResolver(assemblyResolver);
        utils.setVisualStudioProject(visualStudioProjectMock.getMock());
    }
    @Test
    public void resolveAssembly_SimpleConfig_PathIncludesConfig() {

        utils.givenAssembly(null);

        String buildConfiguration="Reality";
        File directory = new File("hoi");
        
        visualStudioProjectMock.givenDirectory(directory);
        visualStudioProjectMock.givenArtifactName(artifactName);
        utils.setBuildConfiguration(buildConfiguration);
        utils.resolveAssembly();
        utils.verifyResolvedAs("hoi\\bin\\Reality\\somename.dll");

        
    }
}
