package com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;

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
    public void testSetMergeByHash() {
        openCoverCommand.setMergeByHash();
        testHelper.assertArgument("-mergebyhash:");      
    }

    @Test
    public void testSetOutputPath() {
        String value="some/path\\with\\backslashes";
        openCoverCommand.setOutputPath(value);
        testHelper.assertArgument("\"-output:some/path/with/backslashes\"");
    }
    
    @Test
    public void testSetExcludeByFileNull_NotInArguments() {
        String value=null;
        openCoverCommand.setExcludeByFileFilter(null);
        testHelper.assertArgumentNotPresent("-excludebyfile");
    }
    
    @Test
    public void testSetExcludeByFileOneArg_ExpectOneArg() {
        List<String> files = new ArrayList<String>();
        files.add("*\\*.Designer.cs");
        openCoverCommand.setExcludeByFileFilter(files);
        testHelper.assertArgument("-excludebyfile:*\\*.Designer.cs");
    }
    
    @Test
    public void testSetExcludeByFileTowArg_ExpectTwoArgsSeperatedBySemic() {
        List<String> files = new ArrayList<String>();
        files.add("*\\*.Designer.cs");
        files.add("johndoe.cs");
        openCoverCommand.setExcludeByFileFilter(files);
        testHelper.assertArgument("-excludebyfile:*\\*.Designer.cs;johndoe.cs");
    }
    
    @Test
    public void testExcludeFromCodeCoverageNotSet_AttributeFilterNotInArgumentList() {
        testHelper.assertArgumentNotPresent("-excludebyattribute");
        
    }
    
    @Test
    public void testExcludeFromCodeCoverageSet_AttributeFilterInArgumentList() {
        openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
        testHelper.assertArgument("-excludebyattribute:*ExcludeFromCodeCoverage*");
    }
}
