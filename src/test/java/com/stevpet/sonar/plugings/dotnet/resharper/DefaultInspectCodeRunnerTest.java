package com.stevpet.sonar.plugings.dotnet.resharper;

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

import com.stevpet.sonar.plugings.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class DefaultInspectCodeRunnerTest {

    private InspectCodeRunner inspectCodeRunner;
    @Mock private Settings settings;
    @Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    @Mock private FileSystem fileSystem;
    @Mock private ReSharperCommandBuilder reSharperCommandBuilder;
    @Mock private Project project;
    @Mock private CommandLineExecutor commandLineExecutor;
    
    @Before
    public void before() {
        org.mockito.MockitoAnnotations.initMocks(this);
        inspectCodeRunner=new DefaultInspectCodeRunner(settings, microsoftWindowsEnvironment, fileSystem, reSharperCommandBuilder,commandLineExecutor);
        
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
        when(settings.getString(ReSharperConstants.INSTALL_DIR_KEY)).thenReturn(testDir.getAbsolutePath());
        inspectCodeRunner.inspectCode();
        
        verify(reSharperCommandBuilder,times(1)).setExecutable(new File(testDir,"inspectcode.exe"));
        verify(commandLineExecutor,times(1)).execute(reSharperCommandBuilder,60);
    }
    
    @Test
    public void basicArgumentsOAndTimeOutnFoundFile() {
        //Given
        File testPath=TestUtils.getResource("/InspectCode/inspectcode.exe");
        File testDir=testPath.getParentFile();
        when(settings.getString(ReSharperConstants.INSTALL_DIR_KEY)).thenReturn(testDir.getAbsolutePath());
        when(settings.getInt(ReSharperConstants.TIMEOUT_MINUTES_KEY)).thenReturn(45);
        //When
        inspectCodeRunner.inspectCode();
        
        verify(commandLineExecutor,times(1)).execute(reSharperCommandBuilder,45);
    }
}