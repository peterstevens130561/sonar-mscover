/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
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
        assertEquals(OpenCoverTestHelper.EXECUTABLE + "/OpenCover.Console.Exe",commandLine);
    }

    @Test
    public void testSetSkipped() {
        String value="all";
        openCoverCommand.setHideSkipped(value);
        testHelper.assertArgument("-hideskipped:" + value);       
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
        testHelper.assertArgument("-excludebyattribute:*.ExcludeFromCoverageAttribute*");
    }
    
    @Test
    public void testSkipAutoPropsSet_SkipAutoPropsInArgumentList() {
        openCoverCommand.setSkipAutoProps();
        testHelper.assertArgument("-skipautoprops");
    }
    
    @Test
    public void testSkipAutoPropsNotSet_SkipAutoPropsNotInArgumentList() {
        testHelper.assertNoArgument();
    }
}
