package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MsCoverPropertiesMock;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.ProjectMock;

public class BinConfigAssemblyResolverTest {

    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    ProjectMock projectMock = new ProjectMock();
    
    @Before() 
    public void before() {
        
    }
    @Test
    public void resolveAssembly_SimpleConfig_PathIncludesConfig() {
        AssemblyResolver assemblyResolver = new BinConfigAssemblyResolver();
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        File assemblyFile = null;
        String buildConfiguration="Reality";
        VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
        File directory = new File("hoi");
        visualStudioProjectMock.givenDirectory(directory);
        String artifactName = "somename.dll";
        visualStudioProjectMock.givenArtifactName(artifactName);
        File result=assemblyResolver.resolveAssembly(assemblyFile, visualStudioProjectMock.getMock(), buildConfiguration);
        assertNotNull(result);
        String createdName=result.getName();
        assertEquals(artifactName,createdName);
        String path=result.getPath();
        assertEquals("hoi\\bin\\Reality\\somename.dll",path);
        
    }
}
