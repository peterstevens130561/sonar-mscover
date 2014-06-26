package com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OpenCoverCommandTest {

    private  OpenCoverTestHelper testHelper = new OpenCoverTestHelper();
    private OpenCoverCommand openCoverCommand ;
    @Before()
    public void before() {
         testHelper.createCommand(); 
         openCoverCommand = testHelper.openCoverCommand;
    }
    
    @Test
    public void CreateCommand_CommandLineShouldHaveExecutable () {
        String commandLine=openCoverCommand.toCommandLine();
        assertNotNull(commandLine);
        assertEquals(testHelper.EXECUTABLE + "/OpenCover.Console.Exe",commandLine);
    }


    @Test
    public void testSetRegister() {
        String value="user";
        openCoverCommand.setRegister(value);
        testHelper.assertArgument("-register:" + value);       
    }

    @Test
    public void testSetTargetDir() {
        String value="some/path\\with\\backslashes and spaces";
        openCoverCommand.setTargetDir(value);
        testHelper.assertArgument("\"-targetdir:some/path/with/backslashes and spaces\"");
    }


    @Test
    public void testSetFilter() {
        String value="+BAAP.NOOT +MIES.WIM";
        openCoverCommand.setFilter(value);
        testHelper.assertArgument("\"-filter:" + value + "\"");      
    }

    @Test
    public void testSetOutputPath() {
        String value="some/path\\with\\backslashes";
        openCoverCommand.setOutputPath(value);
        testHelper.assertArgument("\"-output:some/path/with/backslashes\"");
    }

}
