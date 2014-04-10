/*
 * .NET tools :: Gendarme Runner
 * Copyright (C) 2010 Jose Chillan, Alexandre Victoor and SonarSource
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
package com.stevpet.sonar.plugins.dotnet.mscover.executils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.command.CommandExecutor;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;
import org.sonar.plugins.dotnet.api.utils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Class that runs the Gendarme program.
 */
public class ExecRunner { // NOSONAR : can't mock it otherwise

  private static final Logger LOG = LoggerFactory.getLogger(ExecRunner.class);

  private static final String GENDARME_EXECUTABLE = "gendarme.exe";
  private static final long MINUTES_TO_MILLISECONDS = 60000;
  private static final String EMBEDDED_VERSION = "2.10";

  private File executable;
  private VisualStudioProject vsProject;

  private ExecRunner() {
  }

  /**
   * Creates a new {@link ExecRunner} object for the given executable file. If the executable file does not exist, then the embedded one
   * will be used.
   * 
   * @param execPath
   *          the full path of the gendarme install directory. For instance: "C:/Program Files/gendarme-2.10-bin". May be null: in this
   *          case, the embedded Gendarme executable will be used.
   * @param tempFolder
   *          the temporary folder where the embedded Gendarme executable will be copied if the gendarmePath does not point to a valid
   *          executable
   */
  public static ExecRunner create(String execPath, String tempFolder) throws ExecException {
    ExecRunner runner = new ExecRunner();

    File executable = new File(execPath, GENDARME_EXECUTABLE);
    if (!executable.exists() || !executable.isFile()) {
      LOG.info("Gendarme executable not found: '{}'. The embedded version ({}) will be used instead.", executable.getAbsolutePath(),
          EMBEDDED_VERSION);
      executable = new File(tempFolder, "gendarme-" + EMBEDDED_VERSION + "-bin/" + GENDARME_EXECUTABLE);
      if (!executable.isFile()) {
        LOG.info("Extracting Gendarme binaries in {}", tempFolder);
        extractBinaries(tempFolder);
      }
    }
    runner.executable = executable;

    return runner;
  }

  protected static void extractBinaries(String tempFolder) throws ExecException {
    try {
      URL executableURL = ExecRunner.class.getResource("/gendarme-" + EMBEDDED_VERSION + "-bin");
      ZipUtils.extractArchiveFolderIntoDirectory(StringUtils.substringBefore(executableURL.getFile(), "!").substring(5), "gendarme-"
        + EMBEDDED_VERSION + "-bin", tempFolder);
    } catch (IOException e) {
      throw new ExecException("Could not extract the embedded Gendarme executable: " + e.getMessage(), e);
    }
  }

  /**
   * Creates a pre-configured {@link CommandBuilder} that needs to be completed before running the
   * {@link #execute(CommandBuilder, int)} method.
   * 
   * @param solution
   *          the solution to analyse
   * @param project
   *          the VS project to analyse
   * @return the command to complete.
   */
  public CommandBuilder createCommandBuilder(VisualStudioSolution solution, VisualStudioProject project) {
    this.vsProject = project;
    CommandBuilder builder = CommandBuilder.createBuilder(solution, project);
    builder.setExecutable(executable);
    return builder;
  }

  /**
   * Executes the given Gendarme command.
   * 
   * @param commandBuilder
   *          the gendarmeCommandBuilder
   * @param timeoutMinutes
   *          the timeout for the command
   * @throws ExecException
   *           if Gendarme fails to execute
   */
  public void execute(CommandBuilder commandBuilder, int timeoutMinutes) throws ExecException {
    LOG.debug("Executing Gendarme program...");
    try {
      int exitCode = CommandExecutor.create().execute(commandBuilder.toCommand(), timeoutMinutes * MINUTES_TO_MILLISECONDS);
      // Gendarme returns 1 when the analysis is successful but contains violations, so 1 is valid
      if (exitCode != 0 && exitCode != 1) {
        throw new ExecException(exitCode);
      }
    } finally {
      cleanupFiles(commandBuilder.getBuildConfiguration(), commandBuilder.getBuildPlatform());
    }
  }

  protected void cleanupFiles(String buildConfiguration, String buildPlatform) {
    if (vsProject != null && vsProject.isSilverlightProject()) {
      // need to remove the Silverlight mscorlib.dll
      LOG.debug("Delete Silverlight Mscorlib.dll file");
      FileUtils.deleteQuietly(new File(vsProject.getArtifactDirectory(buildConfiguration, buildPlatform), "mscorlib.dll"));
    }
  }

}
