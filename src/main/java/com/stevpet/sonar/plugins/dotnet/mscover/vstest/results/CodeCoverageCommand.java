package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.Command;

public class CodeCoverageCommand{
    private String coveragePath ;
    private String outputPath;
    private String commandPath ; 
    private static String defaultPath = "C:/Program Files (x86)/Microsoft Visual Studio 11.0/Team Tools/Dynamic Code Coverage Tools/CodeCoverage.exe";
    public CodeCoverageCommand() {
        commandPath=defaultPath;
    }
    
    public void setCoveragePath(String coveragePath) {
        this.coveragePath = coveragePath;
    }
   
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }   
    /**
     * 
     * @param commandPath full path to codecoverage.exe
     */
    public void setCommandPath(String commandPath) {
        this.commandPath = commandPath;
    }
    
    /**
     * @return Creates the commandline with all options
     */
    public String toCommandLine() {
        if(StringUtils.isEmpty(coveragePath) || 
                StringUtils.isEmpty(outputPath) || 
                StringUtils.isEmpty(commandPath)) {
           throw new SonarException("CodeCoverageCommand: invalid usage, not all parameters set"); 
        }
        Command command = Command.create(commandPath);
        command.addArgument("analyze");
        command.addArgument("/output:" + outputPath);
        command.addArgument(coveragePath);
        return command.toCommandLine();
    }


    
}
