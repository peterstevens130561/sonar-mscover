package com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover;

import org.junit.Before;
import org.junit.Test;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;

public class SetTargetCommandTest {
    private  OpenCoverTestHelper testHelper = new OpenCoverTestHelper();
    private OpenCoverCommand openCoverCommand ;
    @Before()
    public void before() {
         testHelper.createCommand(); 
         openCoverCommand = testHelper.openCoverCommand;
    }
    
    @Test
    public void NormalCommand_ShouldSeeCommandLineWithTargetArgsEscaped() {
        OpenCoverTarget target = new Target();
        openCoverCommand.setTargetCommand(target);
        testHelper.assertArgument("\"-target:myexecutable\" \"-targetargs:-aap:\\\"some escaped stuff\\\" -noot:simple\"");
        
    }
    
    private class Target implements OpenCoverTarget {

        public String getExecutable() {
            return "myexecutable";
        }

        public String getArguments() {
            return "-aap:\"some escaped stuff\" -noot:simple";
        }
        
    }
    
}
