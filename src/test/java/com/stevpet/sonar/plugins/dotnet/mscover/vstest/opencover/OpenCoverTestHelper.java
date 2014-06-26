package com.stevpet.sonar.plugins.dotnet.mscover.vstest.opencover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OpenCoverTestHelper {

    protected static String EXECUTABLE = "jippie";
    protected OpenCoverCommand openCoverCommand;

    public OpenCoverTestHelper() {
        openCoverCommand = new OpenCoverCommand(EXECUTABLE);
    }

    protected void assertArgument(String value) {
        String commandLine=openCoverCommand.toCommandLine();
        assertNotNull(commandLine);
        assertEquals(EXECUTABLE + "/OpenCover.Console.Exe " + value,commandLine);
    }

    protected OpenCoverCommand createCommand() {
        OpenCoverCommand openCoverCommand = new OpenCoverCommand(EXECUTABLE);
        return openCoverCommand;
    }

}