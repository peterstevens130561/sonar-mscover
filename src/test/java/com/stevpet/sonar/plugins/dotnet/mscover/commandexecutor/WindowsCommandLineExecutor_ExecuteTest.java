package com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sonar.api.utils.command.CommandExecutor;

import com.stevpet.sonar.plugins.common.api.ShellCommand;
import com.stevpet.sonar.plugins.common.commandexecutor.ImpatientCommandExecutor;
import com.stevpet.sonar.plugins.common.commandexecutor.WindowsCommandLineExecutor;

public class WindowsCommandLineExecutor_ExecuteTest {
    @Mock private ImpatientCommandExecutor commandExecutor ;
    @Mock private ShellCommand shellCommand;
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void executeTestPass() {
        WindowsCommandLineExecutor commandLineExecutor = new WindowsCommandLineExecutor();
        commandLineExecutor.setCommandExecutor(commandExecutor);
        commandLineExecutor.execute(shellCommand);
        
        //verify(commandExecutor,times(1)).execute(eq(shellCommand), stdOut, stdErr, timeoutMilliseconds));
    }
}
