/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import org.junit.Assert;
import org.junit.Test;


import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.CodeCoverageCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter.WindowsCodeCoverageCommand;

public class CodeCoverageCommandTest {
    private static String defaultPath = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/Team Tools/Dynamic Code Coverage Tools/CodeCoverage.exe";
    @Test(expected=IllegalStateException.class)
    public void emptyCommand() {
        WindowsCodeCoverageCommand command = new WindowsCodeCoverageCommand();
        String emptyCommandLine = command.toCommandLine();
        Assert.assertEquals(defaultPath, emptyCommandLine);
    }    
    

    @Test
    public void sunnyCommand() {
        WindowsCodeCoverageCommand command = new WindowsCodeCoverageCommand() ;
        command.setCoveragePath("stevpet.coverage");
        command.setOutputPath("stevpet.xml");
        String commandLine = command.toCommandLine();
        String expected = "null\\CodeCoverage\\CodeCoverage.exe stevpet.coverage stevpet.xml";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test
    public void mixedPath_mustBeWindows() {
        WindowsCodeCoverageCommand command = new WindowsCodeCoverageCommand() ;
        command.setCoveragePath("john/aap\\wim.xml");
        command.setOutputPath("stevpet.xml");
        String commandLine = command.toCommandLine();
        String expected = "null\\CodeCoverage\\CodeCoverage.exe john\\aap\\wim.xml stevpet.xml";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test
    public void spacedCoveragePath_MustBeEnclosedBetweenParenthesis() {
        WindowsCodeCoverageCommand command = new WindowsCodeCoverageCommand() ;
        command.setCoveragePath("john/aap\\wi m.xml");
        command.setOutputPath("stevpet.xml");
        String commandLine = command.toCommandLine();
        String expected = "null\\CodeCoverage\\CodeCoverage.exe \"john\\aap\\wi m.xml\" stevpet.xml";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test
    public void spacedOutputPath_MustBeEnclosedBetweenParenthesis() {
        WindowsCodeCoverageCommand command = new WindowsCodeCoverageCommand() ;
        command.setCoveragePath("john/aap\\wim.xml");
        command.setOutputPath("stev pet.xml");
        String commandLine = command.toCommandLine();
        String expected = "null\\CodeCoverage\\CodeCoverage.exe john\\aap\\wim.xml \"stev pet.xml\"";
        Assert.assertEquals(expected, commandLine);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void invalidCoveragePath_MustThrowException() {
        CodeCoverageCommand command = new WindowsCodeCoverageCommand() ;
        command.setCoveragePath("john\r/aap\\wim.xml");
        Assert.fail();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void invalidOutputPath_MustThrowException() {
        CodeCoverageCommand command = new WindowsCodeCoverageCommand() ;
        command.setOutputPath("john\r/aap\\wim.xml");
        Assert.fail();
    }
}
