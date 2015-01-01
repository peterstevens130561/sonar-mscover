package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;


import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverPropertiesMock;
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
    }
    @Test
    public void resolveAssembly_SimpleConfig_PathIncludesConfig() {
        utils.givenAssembly("somename.dll");
        utils.givenProjectDir(new File("hoi"));
        String buildConfiguration="Reality";
        
        utils.setBuildConfiguration("Debug");
        utils.resolveAssembly();
        utils.verifyResolvedAs("hoi\\bin\\Debug\\somename.dll");

        
    }
}
