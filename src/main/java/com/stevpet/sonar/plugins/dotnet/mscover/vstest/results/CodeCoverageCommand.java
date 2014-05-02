package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.util.FileUtils;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.Command;
import org.sonar.plugins.dotnet.api.utils.ZipUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;

public class CodeCoverageCommand implements ShellCommand {
    private String coveragePath;
    private String outputPath;
    private String sonarPath;
    private static String binaryName = "CodeCoverage";
    private static String binaryFolder = "/" + binaryName;

    CodeCoverageCommand() {
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
     * set the path to the .sonar folder
     * 
     * @param path
     */
    public void setSonarPath(String path) {
        this.sonarPath = path;
    }

    /**
     * @return Creates the commandline with all options
     */
    public String toCommandLine() {
        Command command = toCommand();
        return command.toCommandLine();
    }

    private void removeBinaries(String binaryFolderPath) {
        File binaryFolder = new File(binaryFolderPath);
        if (binaryFolder.exists()) {
            try {
                FileUtils.deleteDirectory(binaryFolderPath);
            } catch (IOException e) {
                throw new MsCoverException("Could not delete directory "
                        + binaryFolderPath + e.getMessage(), e);
            }
        }
    }

    private void extractBinaries(String tempFolder) throws SonarException {
        try {

            URL executableURL = CodeCoverageCommand.class
                    .getResource(binaryFolder);
            String archivePath = StringUtils.substringBefore(
                    executableURL.getFile(), "!").substring(5);
            ZipUtils.extractArchiveFolderIntoDirectory(archivePath, binaryName,
                    tempFolder);
        } catch (IOException e) {
            throw new MsCoverException(
                    "Could not extract the embedded executable: "
                            + e.getMessage(), e);
        }
    }

    /**
     * install the binary from resource by first removing a potential old one, then
     * taking it from the resource jar and put it in the sonar working directory
     */
    public void install() {
        String commandFolderPath = null;
        if (StringUtils.isEmpty(sonarPath)) {
            return ; 
        }
        if(!sonarPath.endsWith("\\.sonar")) {
            throw new MsCoverException("Invalid sonar working directory "
                    + sonarPath);
        }
        commandFolderPath = sonarPath + binaryFolder;
        removeBinaries(commandFolderPath);
        extractBinaries(sonarPath);
    }

    /**
     * 
     * @return complete command
     */
    public Command toCommand() {
        if (StringUtils.isEmpty(coveragePath)
                || StringUtils.isEmpty(outputPath)) {
            throw new MsCoverException(
                    "CodeCoverageCommand: invalid usage, not all parameters set");
        }

        String commandExePath = sonarPath + binaryFolder + "/" + binaryName + ".exe";
        Command command = Command.create(commandExePath);
        command.addArgument(coveragePath);
        command.addArgument(outputPath);
        return command;
    }

}
