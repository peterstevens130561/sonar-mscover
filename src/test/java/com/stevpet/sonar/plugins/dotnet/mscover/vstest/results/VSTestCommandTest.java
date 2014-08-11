package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class VSTestCommandTest {

    private static String EXECUTABLE = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow/vstest.console.exe";
    private VSTestCommand testCommand ;
    @Before
    public void test() {
        testCommand= VSTestCommand.create();
    }
    
    @Test
    public void minimalCommand_OkPath() {             
        platformTest("Any CPU", " /Platform:x64");
    }

    @Test
    public void platform64_OkPath() {
        platformTest("x64"," /Platform:x64");
    }
    
    @Test
    public void platform86_OkPath() {
        platformTest("x86"," /Platform:x86");
    }

    @Test
    public void noplatform_NoPlatformInPath() {
        platformTest(null,"");
    }
    
    @Test
    public void noCodeCoverage_expectNotInCommandLine() {
        checkCodeCoverage("");
    }
    
    @Test
    public void codeCoverageSet_expectInCommandLine() {
        testCommand.setCodeCoverage(true);
        checkCodeCoverage("/EnableCodeCoverage ");
    }
    
    @Test
    public void codeCoverageNotSet_expectNotInCommandLine() {
        testCommand.setCodeCoverage(false);
        checkCodeCoverage("");
    }
     
    private void platformTest(String input, String output) {
        testCommand.setPlatform(input);
        testCommand.setTestSettingsPath("a/b");
        List<String> paths = new ArrayList<String>();
        paths.add("test1");
        paths.add("test2");
        testCommand.setUnitTestAssembliesPath(paths);
        
        String commandLine = testCommand.toCommandLine();
        String expected=EXECUTABLE + output + " \"test1\" \"test2\" /Settings:a/b /Logger:trx";
        assertEquals(expected,commandLine);
    }
    
    private void checkCodeCoverage(String output) {
        testCommand.setTestSettingsPath("");
        List<String> paths = new ArrayList<String>();
        testCommand.setUnitTestAssembliesPath(paths);
        
        String commandLine = testCommand.toCommandLine();
        String expected=EXECUTABLE + " /Settings: " + output + "/Logger:trx";
        assertEquals(expected,commandLine);   
        }

}
