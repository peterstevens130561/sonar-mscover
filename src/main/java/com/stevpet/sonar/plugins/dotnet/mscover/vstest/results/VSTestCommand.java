package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.util.List;

import org.sonar.api.utils.command.Command;

public class VSTestCommand implements ShellCommand {

    private String commandPath;
    private String testSettingsPath;
    private List<String> unitTestAssemblyPaths;
    
    private VSTestCommand() {
        commandPath=defaultPath;
        
    }
    public static VSTestCommand create() {
        return new VSTestCommand();
    }
    
 
    private static String defaultPath = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/" +
            "Common7/IDE/CommonExtensions/Microsoft/TestWindow/vstest.console.exe";
    
    /**
     * @return Creates the commandline with all options
     */
    public String toCommandLine() {
        Command command = toCommand();
        return command.toCommandLine();
    }
    public Command toCommand() {
        Command command = Command.create(commandPath);
        command.addArguments(unitTestAssemblyPaths);
        command.addArgument("/Settings:" + testSettingsPath);
        command.addArgument("/EnableCodeCoverage");
        command.addArgument("/Logger:trx");
        return command;
    }
    public String getTestSettingsPath() {
        return testSettingsPath;
    }
    public void setTestSettingsPath(String testSettingsPath) {
        this.testSettingsPath = testSettingsPath;
    }

    public void setUnitTestAssembliesPath(List<String> unitTestAssemblyPaths) {
        this.unitTestAssemblyPaths = unitTestAssemblyPaths;
    }


}
