package com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;

public class ToCommandTest {
    private  OpenCoverTestHelper testHelper = new OpenCoverTestHelper();
    private OpenCoverCommand openCoverCommand ;
    @Before()
    public void before() {
         testHelper.createCommand(); 
         openCoverCommand = testHelper.openCoverCommand;
    }
    
    @Test
    public void completeTestSequence_CorrectCommand() {
        openCoverCommand.setFilter("myfilter");
        openCoverCommand.setOutputPath("output path");
        openCoverCommand.setRegister("user");
        openCoverCommand.setTargetCommand(new Target());
        Command command = openCoverCommand.toCommand();
        String commandLine=command.toCommandLine();
        Assert.assertTrue(commandLine.contains(" -register:user "));
        Assert.assertTrue(commandLine.contains(" \"-output:output path\" "));
        Assert.assertTrue(commandLine.contains("jippie"));
        
    }
    
    @Test
    public void invokeCommandTwice_shouldBeSame() {
        openCoverCommand.setFilter("myfilter");
        
        String firstCommand = openCoverCommand.toCommandLine();
        String secondCommand = openCoverCommand.toCommandLine();
        assertEquals(firstCommand,secondCommand);
    }
    
        private class Target implements OpenCoverTarget {

            public String getExecutable() {
                return "vstest.console.exe";
            }

            public String getArguments() {
                return "-aap:\"some escaped stuff\" -noot:simple";
            }
            
        }    
    }
