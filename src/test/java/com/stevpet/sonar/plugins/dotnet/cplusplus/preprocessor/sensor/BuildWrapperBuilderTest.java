package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.utils.command.Command;
import org.sonar.test.TestUtils;

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
        File myDir = TestUtils.getResource("BuildWrapper/build-wrapper.exe").getParentFile();
        outputPath = "F:/Development/Solution/wrapper";
        buildWrapperBuilder.setInstallDir(myDir.getAbsolutePath()).setMsBuildOptions("debug crapper").setOutputPath(outputPath);
        //When
        String commandLine=buildWrapperBuilder.toCommandLine();
        //Then
        assertEquals( myDir.getAbsolutePath() + "\\build-wrapper.exe --out-dir \"F:/Development/Solution/wrapper\" msbuild /t:Rebuild debug crapper",commandLine);
        
    }
    
    
    @Test
    public void invalidDirSpecified() {
        //Given
        File myDir = new File("bogusdir");
        outputPath = "F:/Development/Solution/wrapper";
        buildWrapperBuilder.setInstallDir(myDir.getAbsolutePath()).setMsBuildOptions("debug crapper").setOutputPath(outputPath);
        //When
        try {
            buildWrapperBuilder.toCommandLine();
        } catch (BuildWrapperException e) {
            assertTrue("Expect that wrapper is not found",e.getMessage().contains("Executable does not exist"));
            return;
        }
        fail("Wrapper should not be found, expected exception");

    }
}
