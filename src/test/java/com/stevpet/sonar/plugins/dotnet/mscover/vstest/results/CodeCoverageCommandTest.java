package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.utils.SonarException;

public class CodeCoverageCommandTest {
    private static String defaultPath = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/Team Tools/Dynamic Code Coverage Tools/CodeCoverage.exe";
    @Test(expected=SonarException.class)
    public void emptyCommand() {
        CodeCoverageCommand command = new CodeCoverageCommand() ;
        String emptyCommandLine = command.toCommandLine();
        Assert.assertEquals(defaultPath, emptyCommandLine);
    }    
    
    @Test
    public void sunnyCommand() {
        CodeCoverageCommand command = new CodeCoverageCommand() ;
        command.setCoveragePath("stevpet.coverage");
        command.setOutputPath("stevpet.xml");
        String commandLine = command.toCommandLine();
        String expected = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/Team Tools/Dynamic Code Coverage Tools/CodeCoverage.exe analyze /output:stevpet.xml stevpet.coverage";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test
    public void ownCommand() {
        CodeCoverageCommand command = new CodeCoverageCommand() ;
        command.setCoveragePath("stevpet.coverage");
        command.setOutputPath("stevpet.xml");
        command.setCommandPath("CodeCoverage.exe");
        String commandLine = command.toCommandLine();
        String expected = "CodeCoverage.exe analyze /output:stevpet.xml stevpet.coverage";
        Assert.assertEquals(expected, commandLine);
    }
}
