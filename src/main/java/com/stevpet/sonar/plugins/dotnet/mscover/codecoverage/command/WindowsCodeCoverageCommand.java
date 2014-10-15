package com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.util.FileUtils;
import org.sonar.api.utils.SonarException;
import org.sonar.api.utils.command.Command;
import org.sonar.plugins.dotnet.api.utils.ZipUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.commandexecutor.ShellCommand;
import com.stevpet.sonar.plugins.dotnet.mscover.exception.MsCoverException;
import com.stevpet.sonar.plugins.dotnet.mscover.helpers.SonarWindowsFileSystemHelper;

public class WindowsCodeCoverageCommand implements ShellCommand, CodeCoverageCommand {
    private String coveragePath;
    private String outputPath;
    private String sonarPath;
    private static String binaryName = "CodeCoverage";
    private static String binaryFolder = "/" + binaryName;

    public WindowsCodeCoverageCommand() {
    }

    public static WindowsCodeCoverageCommand create() {
        return new WindowsCodeCoverageCommand();
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand#setCoveragePath(java.lang.String)
     */
    @Override
    public void setCoveragePath(String path) {
        SonarWindowsFileSystemHelper.dieOnInvalidPath(path);
        this.coveragePath = SonarWindowsFileSystemHelper.createQualifiedPath(path);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand#setOutputPath(java.lang.String)
     */
    @Override
    public void setOutputPath(String path) {
        SonarWindowsFileSystemHelper.dieOnInvalidPath(path);
        this.outputPath = SonarWindowsFileSystemHelper.createQualifiedPath(path);
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand#setSonarPath(java.lang.String)
     */
    @Override
    public void setSonarPath(String path) {
        this.sonarPath = path;
    }

    /**
     * @return Creates the commandline with all options
     */
    public String toCommandLine() {
        Command command = toCommand();
        return SonarWindowsFileSystemHelper.makeWindowsPath(command.toCommandLine());
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

            URL executableURL = WindowsCodeCoverageCommand.class
                    .getResource(binaryFolder);
            String archivePath = StringUtils.substringBefore(
                    executableURL.getFile().replace("%20", " "), "!").substring(5);
            ZipUtils.extractArchiveFolderIntoDirectory(archivePath, binaryName,
                    tempFolder);
        } catch (IOException e) {
            throw new MsCoverException(
                    "Could not extract the embedded executable: "
                            + e.getMessage(), e);
        }
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand#install()
     */
    @Override
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
