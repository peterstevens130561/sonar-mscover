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
        String expected = "null\\CodeCoverage\\CodeCoverage.exe stevpet.coverage stevpet.xml";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test
    public void mixedPath_mustBeWindows() {
        CodeCoverageCommand command = CodeCoverageCommand.create() ;
        command.setCoveragePath("john/aap\\wim.xml");
        command.setOutputPath("stevpet.xml");
        String commandLine = command.toCommandLine();
        String expected = "null\\CodeCoverage\\CodeCoverage.exe john\\aap\\wim.xml stevpet.xml";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test
    public void spacedCoveragePath_MustBeEnclosedBetweenParenthesis() {
        CodeCoverageCommand command = CodeCoverageCommand.create() ;
        command.setCoveragePath("john/aap\\wi m.xml");
        command.setOutputPath("stevpet.xml");
        String commandLine = command.toCommandLine();
        String expected = "null\\CodeCoverage\\CodeCoverage.exe \"john\\aap\\wi m.xml\" stevpet.xml";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test
    public void spacedOutputPath_MustBeEnclosedBetweenParenthesis() {
        CodeCoverageCommand command = CodeCoverageCommand.create() ;
        command.setCoveragePath("john/aap\\wim.xml");
        command.setOutputPath("stev pet.xml");
        String commandLine = command.toCommandLine();
        String expected = "null\\CodeCoverage\\CodeCoverage.exe john\\aap\\wim.xml \"stev pet.xml\"";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void invalidCoveragePath_MustThrowException() {
        CodeCoverageCommand command = CodeCoverageCommand.create() ;
        command.setCoveragePath("john\r/aap\\wim.xml");
        Assert.fail();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void invalidOutputPath_MustThrowException() {
        CodeCoverageCommand command = CodeCoverageCommand.create() ;
        command.setOutputPath("john\r/aap\\wim.xml");
        Assert.fail();
    }
}
