package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.CommandHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverTarget;

public class VSTestCommand implements ShellCommand,OpenCoverTarget {

    private static String defaultPath = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/" +
            "Common7/IDE/CommonExtensions/Microsoft/TestWindow/vstest.console.exe";
    private String commandPath;
    private String testSettingsPath;
    private List<String> unitTestAssemblyPaths;
    private boolean doCodeCoverage;
    private String platform;
    
    private VSTestCommand() {
        commandPath=defaultPath;
        
    }
    public static VSTestCommand create() {
        return new VSTestCommand();
    }
    
  
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
        if (doCodeCoverage) {
            command.addArgument("/EnableCodeCoverage");
        }
        command.addArgument("/Logger:trx");
        addPlatformIfSpecified(command);
        return command;
    }
    
    private void addPlatformIfSpecified(Command command) {
        if(StringUtils.isEmpty(platform)) {
            return;
        }
        platform=platform.replaceAll("\\s", "");
        if("anycpu".equalsIgnoreCase(platform)) {
            platform="x64";
        }
        command.addArgument("/Platform:" + platform);
    }
    
    public String getTestSettingsPath() {
        return testSettingsPath;
    }
    public void setTestSettingsPath(String testSettingsPath) {
        this.testSettingsPath = testSettingsPath;
    }

    public void setUnitTestAssembliesPath(List<String> unitTestAssemblyPaths) {
        this.unitTestAssemblyPaths = CommandHelper.parenthesizeArguments(unitTestAssemblyPaths);
    }
    
    public String getExecutable() {
        return commandPath;
    }
    public String getArguments() {
        Command command = toCommand();
        String commandLine=command.toCommandLine();
        int start = commandPath.length() + 1;
        return commandLine.substring(start);
    }
    public void setCodeCoverage(boolean doCodeCoverage) {
        this.doCodeCoverage=doCodeCoverage;
    }
    public void setPlatform(String platform) {
        this.platform=platform;
        
    }


}
