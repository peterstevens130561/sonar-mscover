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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.utils.command.Command;

import com.stevpet.sonar.plugins.dotnet.mscover.helpers.SonarWindowsFileSystemHelper;

public class WindowsCodeCoverageCommand extends CodeCoverageCommand {
    private String coveragePath;
    private String outputPath;
    private String sonarPath;
    private static String binaryName = "CodeCoverage";
    private static String binaryFolder = "/" + binaryName;

    public WindowsCodeCoverageCommand() {
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand#setCoveragePath(java.lang.String)
     */
    public void setCoveragePath(String path) {
        SonarWindowsFileSystemHelper.dieOnInvalidPath(path);
        this.coveragePath = SonarWindowsFileSystemHelper.createQualifiedPath(path);
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand#setOutputPath(java.lang.String)
     */
    public void setOutputPath(String path) {
        SonarWindowsFileSystemHelper.dieOnInvalidPath(path);
        this.outputPath = SonarWindowsFileSystemHelper.createQualifiedPath(path);
    }


    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand#setSonarPath(java.lang.String)
     */
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
                FileUtils.deleteDirectory(binaryFolder);
            } catch (IOException e) {
                throw new IllegalStateException("Could not delete directory "
                        + binaryFolderPath + e.getMessage(), e);
            }
        }
    }

    private void extractBinaries(String tempFolder)  {
        try {

            URL executableURL = WindowsCodeCoverageCommand.class
                    .getResource(binaryFolder);
            String archivePath = StringUtils.substringBefore(
                    executableURL.getFile().replace("%20", " "), "!").substring(5);
            ZipUtils.extractArchiveFolderIntoDirectory(archivePath, binaryName,
                    tempFolder);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not extract the embedded executable: "
                            + e.getMessage(), e);
        }
    }

    /* (non-Javadoc)
     * @see com.stevpet.sonar.plugins.dotnet.mscover.codecoverage.command.CodeCoverageCommand#install()
     */
    public void install() {
        String commandFolderPath = null;
        if (StringUtils.isEmpty(sonarPath)) {
            return ; 
        }
        if(!sonarPath.endsWith("\\.sonar")) {
            throw new IllegalStateException("Invalid sonar working directory "
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
            throw new IllegalStateException(
                    "CodeCoverageCommand: invalid usage, not all parameters set");
        }

        String commandExePath = sonarPath + binaryFolder + "/" + binaryName + ".exe";
        Command command = Command.create(commandExePath);
        command.addArgument(coveragePath);
        command.addArgument(outputPath);
        return command;
    }

    @Override
    public String getExecutable() {
        return binaryName;
    }

}
