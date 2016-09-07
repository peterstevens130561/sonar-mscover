/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class ProcessPropertiesTest {
    private static final String BLOCK1 = "\r\n" +
            "\r\n" + 
            "CommandLine=C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe \"-output:C:/Development/Radiant/Main/Tests/SpecflowTests/Api/Math.Geometry/.sonar/coverage_FluidModeling.SpecflowTest.xml\" -excludebyfile:*\\*.Designer.cs -hideskipped:all \"-targetargs:\\\"C:\\Development\\Radiant\\Main\\bin\\Release\\FluidModeling.SpecflowTest.dll\\\" /Settings:C:\\Development\\Radiant\\Main\\Tests\\unittests.runsettings /Logger:trx /Platform:x64 /inIsolation /TestCaseFilter:\\\"(TestCategory=RegressionTest|TestCategory=AcceptanceTest)\\\"\" -mergebyhash: -skipautoprops -register:Path64 \"-target:C:/Program Files (x86)/Microsoft Visual Studio 14.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow\\vstest.console.exe\" \"-targetdir:C:/Development/Radiant/Main/bin/Release\" -excludebyattribute:*.ExcludeFromCoverageAttribute*\r\n" +
            "CSName=RDSJ741TY1\r\n" +
            "Description=OpenCover.Console.exe\r\n" +
            "ExecutablePath=C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe\r\n" +
            "ExecutionState=\r\n" +
            "Handle=19272\r\n" +
            "HandleCount=243\r\n" +
            "InstallDate=\r\n" +
            "KernelModeTime=47112302\r\n" +
            "MaximumWorkingSetSize=1380\r\n" +
            "MinimumWorkingSetSize=200\r\n" +
            "Name=OpenCover.Console.exe\r\n" +
            "OSName=Microsoft Windows 7 Enterprise |C:\\windows|\\Device\\Harddisk1\\Partition1\r\n" +
            "OtherOperationCount=10823\r\n" +
            "OtherTransferCount=5033476\r\n" +
            "PageFaults=846652\r\n" +
            "PageFileUsage=615172\r\n" +
            "ParentProcessId=19180\r\n" +
            "PeakPageFileUsage=850384\r\n" +
            "PeakVirtualSize=1260781568\r\n" +
            "PeakWorkingSetSize=846812\r\n" +
            "Priority=8\r\n" +
            "PrivatePageCount=629936128\r\n" +
            "ProcessId=19272\r\n" +
            "QuotaNonPagedPoolUsage=61\r\n" +
            "QuotaPagedPoolUsage=285\r\n" +
            "QuotaPeakNonPagedPoolUsage=62\r\n" +
            "QuotaPeakPagedPoolUsage=287\r\n" +
            "ReadOperationCount=236044\r\n" +
            "ReadTransferCount=1102747794\r\n" +
            "SessionId=1\r\n" +
            "Status=\r\n" +
            "TerminationDate=\r\n" +
            "ThreadCount=9\r\n" +
            "UserModeTime=779380996\r\n" +
            "VirtualSize=1256587264\r\n" +
            "WindowsVersion=6.1.7601\r\n" +
            "WorkingSetSize=553459712\r\n" +
            "WriteOperationCount=3\r\n" +
            "WriteTransferCount=133\r\n";
    private ProcessProperties processProperties;
    
    @Test
    public void createTest() {
        try {
            new ProcessProperties(null);
        } catch (IllegalArgumentException e) {
            return;
        } fail("Expected IllegalArgumentException, as the constructor expects process properties");
    }
    
    @Before
    public void before() {
        processProperties = new ProcessProperties(BLOCK1);
    }
    
    @Test
    public void mwicProcessHas40Properties() {
        assertEquals(40,processProperties.size());
    }
    
    @Test
    public void checkFirstPropertyForExistence() {
        String commandLine=processProperties.getCommandLine();
        assertEquals("C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe \"-output:C:/Development/Radiant/Main/Tests/SpecflowTests/Api/Math.Geometry/.sonar/coverage_FluidModeling.SpecflowTest.xml\" -excludebyfile:*\\*.Designer.cs -hideskipped:all \"-targetargs:\\\"C:\\Development\\Radiant\\Main\\bin\\Release\\FluidModeling.SpecflowTest.dll\\\" /Settings:C:\\Development\\Radiant\\Main\\Tests\\unittests.runsettings /Logger:trx /Platform:x64 /inIsolation /TestCaseFilter:\\\"(TestCategory=RegressionTest|TestCategory=AcceptanceTest)\\\"\" -mergebyhash: -skipautoprops -register:Path64 \"-target:C:/Program Files (x86)/Microsoft Visual Studio 14.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow\\vstest.console.exe\" \"-targetdir:C:/Development/Radiant/Main/bin/Release\" -excludebyattribute:*.ExcludeFromCoverageAttribute*",commandLine);
    }
    
    @Test
    public void checkProcessId() {
        String processId=processProperties.getProcessId();
        assertEquals("19272",processId);
    }
    
    @Test
    public void checkParentProcessId() {
        String parentProcessId = processProperties.getParentProcessId();
        assertEquals("19180",parentProcessId);
        
    }
    
}
