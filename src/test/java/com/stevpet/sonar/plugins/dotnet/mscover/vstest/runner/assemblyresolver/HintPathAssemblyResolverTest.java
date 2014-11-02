package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner.assemblyresolver;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor.MsCoverPropertiesMock;

public class HintPathAssemblyResolverTest {

    MsCoverPropertiesMock msCoverPropertiesMock = new MsCoverPropertiesMock();
    VisualStudioProjectMock visualStudioProjectMock = new VisualStudioProjectMock();
    HintPathAssemblyResolver hintPathAssemblyResolver = new HintPathAssemblyResolver();
    
    @Before()
    public void before() {
        hintPathAssemblyResolver.setMsCoverProperties(msCoverPropertiesMock.getMock());
    }
    
    @Test
    public void resolveAssembly_NoHintPathDefined_ReturnFile() {
        String fileName = "myfilename" ;
        String artifactName="artifactName";
        
        msCoverPropertiesMock.givenUnitTestHintPath(null);
        File assemblyFile = new File(fileName);
        visualStudioProjectMock.givenArtifactName(artifactName);
        
        File resultFile=hintPathAssemblyResolver.resolveAssembly(assemblyFile, visualStudioProjectMock.getMock(), null);
        
        assertEquals(assemblyFile,resultFile);
        
    }
    
    @Test
    public void resolveAssembly_HitPathDefined_ReturnHintPath() {
        String fileName = "myfilename" ;
        String artifactName = "artifactName";
        String hintPath="C:/Development/Jewel.Release.Oahu/JewelEarth/bin";
        msCoverPropertiesMock.givenUnitTestHintPath(hintPath);
        File assemblyFile = new File(fileName);
        visualStudioProjectMock.givenArtifactName(artifactName); 
        
        File resultFile=hintPathAssemblyResolver.resolveAssembly(assemblyFile, visualStudioProjectMock.getMock(), null);
        
        assertNotNull(resultFile);
        assertEquals(artifactName,resultFile.getName());
        assertEquals(hintPath.replaceAll("/","\\\\") + "\\" +artifactName,resultFile.getPath());
    }
}
