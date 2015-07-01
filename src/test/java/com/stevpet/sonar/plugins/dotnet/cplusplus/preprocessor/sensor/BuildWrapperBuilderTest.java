package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class BuildWrapperBuilderTest {

    private BuildWrapperBuilder buildWrapperBuilder;
    private String outputPath;

    @Before
    public void before() {
        this.buildWrapperBuilder = new BuildWrapperBuilder();
        
    }
    
    @Test
    public void normalUse() {
        //Given
        File myDir = new File("mydir");
        outputPath = "F:/Development/Solution/wrapper";
        buildWrapperBuilder.setInstallDir(myDir.getAbsolutePath()).setMsBuildOptions("debug crapper").setOutputPath(outputPath).setMsBuildDir("fun");
        //When
        String commandLine=buildWrapperBuilder.toCommandLine();
        //Then
        assertEquals(myDir.getAbsolutePath() + "\\build-wrapper.exe --out-dir \"F:/Development/Solution/wrapper\" \"fun\\msbuild\" /t:Rebuild debug crapper",commandLine);
        
    }
}
