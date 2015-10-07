package com.stevpet.sonar.plugins.dotnet.resharper;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;
import org.sonar.test.TestUtils;

import com.stevpet.sonar.plugins.common.api.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.resharper.DefaultInspectCodeRunner;
import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeBatchData;
import com.stevpet.sonar.plugins.dotnet.resharper.InspectCodeRunner;
import com.stevpet.sonar.plugins.dotnet.resharper.ReSharperConfiguration;
import com.stevpet.sonar.plugins.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultInspectCodeRunnerTest {

    private InspectCodeRunner inspectCodeRunner;
    @Mock private Settings settings;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private FileSystem fileSystem;
    @Mock private ReSharperCommandBuilder reSharperCommandBuilder;
    @Mock private Project project;
    @Mock private CommandLineExecutor commandLineExecutor;
    @Mock private ReSharperConfiguration reSharperConfiguration;
    private InspectCodeBatchData inspectCodeBatchData;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        inspectCodeBatchData = new InspectCodeBatchData();
        inspectCodeRunner=new DefaultInspectCodeRunner(settings, microsoftWindowsEnvironment, fileSystem, reSharperCommandBuilder,commandLineExecutor, reSharperConfiguration,inspectCodeBatchData);
        
    }
    
    @Test
    public void noArgumentsExpectException() {
        try {
        inspectCodeRunner.inspectCode();
        } catch ( SonarException e ) {
            assertTrue("should fail on inspectCode not found",e.getMessage().contains("inspectcode not found"));
            return;
        }
        fail("should have failed with SonarException");
    }
    
    @Test
    public void basicArgumentsOnFoundFile() {
        File testPath=TestUtils.getResource("/InspectCode/inspectcode.exe");
        File testDir=testPath.getParentFile();
        when(reSharperConfiguration.getInspectCodeInstallDir()).thenReturn(testDir.getAbsolutePath());
        when(reSharperConfiguration.getTimeOutMinutes()).thenReturn(20);
        inspectCodeRunner.inspectCode();
        
        verify(reSharperCommandBuilder,times(1)).setExecutable(new File(testDir,"inspectcode.exe"));
        verify(commandLineExecutor,times(1)).execute(reSharperCommandBuilder,20);
    }
    
    @Test
    public void cacheSetShouldNotBeInCommandLine() {
        File testPath=TestUtils.getResource("/InspectCode/inspectcode.exe");
        File testDir=testPath.getParentFile();
        when(reSharperConfiguration.getInspectCodeInstallDir()).thenReturn(testDir.getAbsolutePath());
        when(reSharperConfiguration.getTimeOutMinutes()).thenReturn(20);
        when(reSharperConfiguration.useCache()).thenReturn(true);
        when(reSharperConfiguration.getCachesHome()).thenReturn("bla");
        
        inspectCodeRunner.inspectCode();
        
        verify(reSharperCommandBuilder,times(1)).setCachesHome("bla");  
    }
    
    @Test
    public void noCacheSetShouldNotBeInCommandLine() {
        File testPath=TestUtils.getResource("/InspectCode/inspectcode.exe");
        File testDir=testPath.getParentFile();
        when(reSharperConfiguration.getInspectCodeInstallDir()).thenReturn(testDir.getAbsolutePath());
        when(reSharperConfiguration.getTimeOutMinutes()).thenReturn(20);
        when(reSharperConfiguration.useCache()).thenReturn(true);
        when(reSharperConfiguration.getCachesHome()).thenReturn(null);
        
        inspectCodeRunner.inspectCode();
        
        verify(reSharperCommandBuilder,times(1)).setCachesHome(null);  
    }
    
    @Test
    public void doNotUseCacheShouldReferToSonarCommandLine() {
        File testPath=TestUtils.getResource("/InspectCode/inspectcode.exe");
        File testDir=testPath.getParentFile();
        when(reSharperConfiguration.getInspectCodeInstallDir()).thenReturn(testDir.getAbsolutePath());
        when(reSharperConfiguration.getTimeOutMinutes()).thenReturn(20);
        when(reSharperConfiguration.getCachesHome()).thenReturn(null);
        when(reSharperConfiguration.useCache()).thenReturn(false);
        File workDir=new File(".sonar");
        when(fileSystem.workDir()).thenReturn(workDir);
        
        inspectCodeRunner.inspectCode();
        
        verify(reSharperCommandBuilder,times(1)).setCachesHome(workDir.getAbsolutePath() + "\\inspectcode_cache");  
    }
    @Test
    public void basicArgumentsOAndTimeOutnFoundFile() {
        //Given
        File testPath=TestUtils.getResource("/InspectCode/inspectcode.exe");
        File testDir=testPath.getParentFile();
        when(reSharperConfiguration.getInspectCodeInstallDir()).thenReturn(testDir.getAbsolutePath());
        when(reSharperConfiguration.getTimeOutMinutes()).thenReturn(45);
        //When
        inspectCodeRunner.inspectCode();
        
        verify(commandLineExecutor,times(1)).execute(reSharperCommandBuilder,45);
    }
}
