package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.utils.SonarException;

public class CodeCoverageCommandTest {
    private static String defaultPath = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/Team Tools/Dynamic Code Coverage Tools/CodeCoverage.exe";
    @Test(expected=SonarException.class)
    public void emptyCommand() {
        CodeCoverageCommand command = CodeCoverageCommand.create();
        String emptyCommandLine = command.toCommandLine();
        Assert.assertEquals(defaultPath, emptyCommandLine);
    }    
    
    @Test
    public void sunnyCommand() {
        CodeCoverageCommand command = CodeCoverageCommand.create() ;
        command.setCoveragePath("stevpet.coverage");
        command.setOutputPath("stevpet.xml");
        String commandLine = command.toCommandLine();
        String expected = "/CodeCoverage/CodeCoverage.exe stevpet.coverage stevpet.xml";
        Assert.assertEquals(expected, commandLine);
    }
    
}
