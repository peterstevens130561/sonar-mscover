package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MsCoverPropertiesMock;

public class HintPathAssemblyResolverTest {

    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
    HintPathAssemblyResolver assemblyResolver = new HintPathAssemblyResolver();
    AssemblyResolverTestUtils utils = new AssemblyResolverTestUtils();
    private String fileName = "unittest.dll";
    private String artifactName="artifactName";
    private String hintPath="C:/Development/Jewel.Release.Oahu/JewelEarth/bin";
    
    @Before()
    public void before() {
        assemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
        
        utils.setAssemblyResolver(assemblyResolver);
        utils.givenAssembly(fileName);
        utils.setVisualStudioProject(visualStudioProjectMock.getMock());

        visualStudioProjectMock.givenArtifactName(artifactName);
    }
    
    @Test
    public void resolveAssembly_NoHintPathDefined_ReturnFile() {  
        utils.resolveAssembly();      
        utils.verifyNotResolved();    
    }
    
    @Test
    public void resolveAssembly_HitPathDefined_ReturnHintPath() {

        msCoverPropertiesMock.givenUnitTestHintPath(hintPath);
     
        utils.resolveAssembly(); 
        utils.verifyResolvedAs(hintPath + "\\" + artifactName);

    }
}
