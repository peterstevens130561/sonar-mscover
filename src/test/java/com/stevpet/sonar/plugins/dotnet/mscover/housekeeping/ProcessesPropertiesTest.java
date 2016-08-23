package com.stevpet.sonar.plugins.dotnet.mscover.housekeeping;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;

public class ProcessesPropertiesTest {

    private static final String BLOCKSEPERATOR = "\r\n\r\n\r\n\r\n";
    private static final String BLOCK3 = BLOCKSEPERATOR +
                "CommandLine=C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe \"-output:C:/Development/Radiant/Main/Tests/SpecflowTests/Api/Math.Geometry/.sonar/coverage_Volumetrics.SpecflowTest.xml\" -excludebyfile:*\\*.Designer.cs -hideskipped:all \"-targetargs:\\\"C:\\Development\\Radiant\\Main\\bin\\Release\\Volumetrics.SpecflowTest.dll\\\" /Settings:C:\\Development\\Radiant\\Main\\Tests\\unittests.runsettings /Logger:trx /Platform:x64 /inIsolation /TestCaseFilter:\\\"(TestCategory=RegressionTest|TestCategory=AcceptanceTest)\\\"\" -mergebyhash: -skipautoprops -register:Path64 \"-target:C:/Program Files (x86)/Microsoft Visual Studio 14.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow\\vstest.console.exe\" \"-targetdir:C:/Development/Radiant/Main/bin/Release\" -excludebyattribute:*.ExcludeFromCoverageAttribute*\r\n" +
                "CSName=RDSJ741TY1\r\n" +
                "Description=OpenCover.Console.exe\r\n" +
                "ExecutablePath=C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe\r\n" +
                "ExecutionState=\r\n" +
                "Handle=20132\r\n" +
                "HandleCount=244\r\n" +
                "InstallDate=\r\n" +
                "KernelModeTime=56160360\r\n" +
                "MaximumWorkingSetSize=1380\r\n" +
                "MinimumWorkingSetSize=200\r\n" +
                "Name=OpenCover.Console.exe\r\n" +
                "OSName=Microsoft Windows 7 Enterprise |C:\\windows|\\Device\\Harddisk1\\Partition1\r\n" +
                "OtherOperationCount=15217\r\n" +
                "OtherTransferCount=5049866\r\n" +
                "PageFaults=704914\r\n" +
                "PageFileUsage=531416\r\n" +
                "ParentProcessId=19180\r\n" +
                "PeakPageFileUsage=604472\r\n" +
                "PeakVirtualSize=853606400\r\n" +
                "PeakWorkingSetSize=611540\r\n" +
                "Priority=8\r\n" +
                "PrivatePageCount=544169984\r\n" +
                "ProcessId=20132\r\n" +
                "QuotaNonPagedPoolUsage=49\r\n" +
                "QuotaPagedPoolUsage=289\r\n" +
                "QuotaPeakNonPagedPoolUsage=51\r\n" +
                "QuotaPeakPagedPoolUsage=291\r\n" +
                "ReadOperationCount=235757\r\n" +
                "ReadTransferCount=1101455506\r\n" +
                "SessionId=1\r\n" +
                "Status=\r\n" +
                "TerminationDate=\r\n" +
                "ThreadCount=9\r\n" +
                "UserModeTime=665500266\r\n" +
                "VirtualSize=846856192\r\n" +
                "WindowsVersion=6.1.7601\r\n" +
                "WorkingSetSize=553455616\r\n" +
                "WriteOperationCount=3\r\n" +
                "WriteTransferCount=133\r\n";
    private static final String BLOCK2 = BLOCKSEPERATOR +
                "CommandLine=C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe \"-output:C:/Development/Radiant/Main/Tests/SpecflowTests/Api/Math.Geometry/.sonar/coverage_ReservoirSimulation.SpecflowTest.xml\" -excludebyfile:*\\*.Designer.cs -hideskipped:all \"-targetargs:\\\"C:\\Development\\Radiant\\Main\\bin\\Release\\ReservoirSimulation.SpecflowTest.dll\\\" /Settings:C:\\Development\\Radiant\\Main\\Tests\\unittests.runsettings /Logger:trx /Platform:x64 /inIsolation /TestCaseFilter:\\\"(TestCategory=RegressionTest|TestCategory=AcceptanceTest)\\\"\" -mergebyhash: -skipautoprops -register:Path64 \"-target:C:/Program Files (x86)/Microsoft Visual Studio 14.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow\\vstest.console.exe\" \"-targetdir:C:/Development/Radiant/Main/bin/Release\" -excludebyattribute:*.ExcludeFromCoverageAttribute*\r\n" +
                "CSName=RDSJ741TY1\r\n" +
                "Description=OpenCover.Console.exe\r\n" +
                "ExecutablePath=C:\\users\\stevpet\\AppData\\Local\\Apps\\OpenCover\\OpenCover.Console.Exe\r\n" +
                "ExecutionState=\r\n" +
                "Handle=19412\r\n" +
                "HandleCount=243\r\n" +
                "InstallDate=\r\n" +
                "KernelModeTime=50076321\r\n" +
                "MaximumWorkingSetSize=1380\r\n" +
                "MinimumWorkingSetSize=200\r\n" +
                "Name=OpenCover.Console.exe\r\n" +
                "OSName=Microsoft Windows 7 Enterprise |C:\\windows|\\Device\\Harddisk1\\Partition1\r\n" +
                "OtherOperationCount=10846\r\n" +
                "OtherTransferCount=5038762\r\n" +
                "PageFaults=839854\r\n" +
                "PageFileUsage=610768\r\n" +
                "ParentProcessId=19180\r\n" +
                "PeakPageFileUsage=845884\r\n" +
                "PeakVirtualSize=1260781568\r\n" +
                "PeakWorkingSetSize=811664\r\n" +
                "Priority=8\r\n" +
                "PrivatePageCount=625426432\r\n" +
                "ProcessId=19412\r\n" +
                "QuotaNonPagedPoolUsage=61\r\n" +
                "QuotaPagedPoolUsage=289\r\n" +
                "QuotaPeakNonPagedPoolUsage=62\r\n" +
                "QuotaPeakPagedPoolUsage=291\r\n" +
                "ReadOperationCount=236109\r\n" +
                "ReadTransferCount=1102998162\r\n" +
                "SessionId=1\r\n" +
                "Status=\r\n" +
                "TerminationDate=\r\n" +
                "ThreadCount=9\r\n" +
                "UserModeTime=783281021\r\n" +
                "VirtualSize=1256587264\r\n" +
                "WindowsVersion=6.1.7601\r\n" +
                "WorkingSetSize=534892544\r\n" +
                "WriteOperationCount=3\r\n" +
                "WriteTransferCount=133\r\n";
    private static final String BLOCK1 = BLOCKSEPERATOR +
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
    
    String result=BLOCK1 + 
            BLOCK2 +
            BLOCK3 +
            "\r\n" +
            "" ;
    @Mock private CommandLineExecutor executor;
    private ProcessesProperties processes;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        MwicBridge mwicBridge = new MwicBridge(executor);
        when(executor.getStdOut()).thenReturn(result);
        processes = mwicBridge.getProcessPropertiesForName("opencover.console.exe");
    }
    
    
    @Test
    public void mwicReceived3Processes() {
        assertEquals(3,processes.size());
    }
    
    @Test
    public void mwicFindProcess() {
        String commandLine="C:/users/stevpet/AppData/Local/Apps/OpenCover/OpenCover.Console.Exe \"-output:C:/Development/Radiant/Main/Tests/SpecflowTests/Api/Math.Geometry/.sonar/coverage_ReservoirSimulation.SpecflowTest.xml\" -excludebyfile:*\\*.Designer.cs -hideskipped:all \"-targetargs:\\\"C:\\Development\\Radiant\\Main\\bin\\Release\\ReservoirSimulation.SpecflowTest.dll\\\" /Settings:C:\\Development\\Radiant\\Main\\Tests\\unittests.runsettings /Logger:trx /Platform:x64 /inIsolation /TestCaseFilter:\\\"(TestCategory=RegressionTest|TestCategory=AcceptanceTest)\\\"\" -mergebyhash: -skipautoprops -register:Path64 \"-target:C:/Program Files (x86)/Microsoft Visual Studio 14.0/Common7/IDE/CommonExtensions/Microsoft/TestWindow\\vstest.console.exe\" \"-targetdir:C:/Development/Radiant/Main/bin/Release\" -excludebyattribute:*.ExcludeFromCoverageAttribute*";
        String id=processes.getProcessIdOfCommandLine(commandLine);
        assertEquals("19412",id);
    }
}
