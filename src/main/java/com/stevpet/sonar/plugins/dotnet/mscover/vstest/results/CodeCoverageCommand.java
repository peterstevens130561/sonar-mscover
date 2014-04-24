package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.Command;
import org.sonar.plugins.dotnet.api.utils.ZipUtils;


public class CodeCoverageCommand implements ShellCommand {
    private String coveragePath ;
    private String outputPath;
    private String commandPath ; 
    private static String defaultPath = "C:/Program Files (x86)/Baker Hughes/Coverage2Xml/CodeCoverage.exe";
    CodeCoverageCommand() {
        commandPath=defaultPath;
    }
    
    public static CodeCoverageCommand create() {
        return new CodeCoverageCommand();
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
        Command command = toCommand();
        return command.toCommandLine();
    }

    private String extractBinaries(String tempFolder) {
        try {
          URL executableURL = CodeCoverageCommand.class.getResource("/CodeCoverage");
          String urlFile = executableURL.getFile().substring(1);
          //String source=StringUtils.substringBefore(executableURL.getFile(), "!").substring(5);
          ZipUtils.extractArchiveFolderIntoDirectory(urlFile, "Release", tempFolder);
        } catch (IOException e) {
          throw new SonarException("Could not extract the embedded Gendarme executable: " + e.getMessage(), e);
        }
        return tempFolder + "/Release";
      }
    /**
     * 
     * @return complete command
     */
    public Command toCommand() {
        //extractBinaries(System.getenv("TMP") + "/Code");
        if(StringUtils.isEmpty(coveragePath) || 
                StringUtils.isEmpty(outputPath) || 
                StringUtils.isEmpty(commandPath)) {
           throw new SonarException("CodeCoverageCommand: invalid usage, not all parameters set"); 
        }
        Command command = Command.create(commandPath);
        command.addArgument(coveragePath);
        command.addArgument(outputPath);
        return command;
    }
    




    
}
