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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.command.Command;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;
import org.sonar.plugins.dotnet.api.tools.CilRuleEngineCommandBuilderSupport;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Class used to build the command line to run Gendarme.
 */
public final class CommandBuilder extends CilRuleEngineCommandBuilderSupport {

  private static final Logger LOG = LoggerFactory.getLogger(CommandBuilder.class);

  private File silverlightFolder;
  private String gendarmeConfidence = "normal+";
  private String gendarmeSeverity = "all";

  private CommandBuilder() {
  }

  /**
   * Constructs a {@link CommandBuilder} object for the given Visual Studio solution.
   * 
   * @param solution
   *          the solution to analyze
   *  @param project
   *          the VS project to analyze
   *          
   * @return a Gendarme builder for this solution
   */
  public static CommandBuilder createBuilder(VisualStudioSolution solution, VisualStudioProject project) {
    CommandBuilder builder = new CommandBuilder();
    builder.solution = solution;
    builder.vsProject = project;
    return builder;
  }

  /**
   * Sets the Silverlight folder where is located the
   * Silverlight version of mscorlib.
   * 
   * @param silverlightFolder
   *          the Silverlight folder
   */
  public void setSilverlightFolder(File silverlightFolder) {
    this.silverlightFolder = silverlightFolder;
  }

  /**
   * Sets the Gendarme confidence level. By default "normal+" if nothing is specified.
   * 
   * @param gendarmeConfidence
   *          the confidence level
   * @return the current builder
   */
  public void setConfidence(String gendarmeConfidence) {
    this.gendarmeConfidence = gendarmeConfidence;

  }

  /**
   * Sets the Gendarme severity level. By default "all" if nothing is specified.
   * 
   * @param gendarmeSeverity
   *          the severity level
   * 
   */
  public void setSeverity(String gendarmeSeverity) {
    this.gendarmeSeverity = gendarmeSeverity;
  }

  protected String getBuildConfiguration() {
    return buildConfiguration;
  }

  protected String getBuildPlatform() {
    return buildPlatform;
  }

  /**
   * Transforms this command object into a Command object that can be passed to the CommandExecutor.
   * 
   * @return the Command object that represents the command to launch.
   */
  public Command toCommand() throws ExecException {
    Collection<File> assemblyToScanFiles = findAssembliesToScan();
    validate(assemblyToScanFiles);

    LOG.debug("- Gendarme program    : " + executable);
    Command command = Command.create(executable.getAbsolutePath());

    LOG.debug("- Config file         : " + configFile);
    command.addArgument("--config");
    command.addArgument(configFile.getAbsolutePath());

    LOG.debug("- Report file         : " + reportFile);
    command.addArgument("--xml");
    command.addArgument(reportFile.getAbsolutePath());

    LOG.debug("- Quiet output");
    command.addArgument("--quiet");

    LOG.debug("- Confidence          : " + gendarmeConfidence);
    command.addArgument("--confidence");
    command.addArgument(gendarmeConfidence);

    LOG.debug("- Severity            : all");
    command.addArgument("--severity");
    command.addArgument(gendarmeSeverity);

    LOG.debug("- Scanned assemblies  :");
    for (File checkedAssembly : assemblyToScanFiles) {
      LOG.debug("   o " + checkedAssembly);
      command.addArgument(checkedAssembly.getAbsolutePath());
    }

    if (vsProject != null && vsProject.isSilverlightProject()) {
      copySilverlightAssembly();
    }

    return command;
  }

  protected void copySilverlightAssembly() throws ExecException {
    File silverlightAssembly = new File(silverlightFolder, "mscorlib.dll");
    if (silverlightAssembly == null || !silverlightAssembly.isFile()) {
      throw new ExecException("Could not find Silverlight Mscorlib.dll assembly. Please check your settings.");
    }
    File destinationDirectory = vsProject.getArtifactDirectory(buildConfiguration, buildPlatform);
    if (destinationDirectory == null) {
      throw new ExecException("Impossible to copy Silverlight Mscorlib.dll as there is no existing artifact "
        + "directory for the build configuration: " + buildConfiguration);
    }
    try {
      LOG.debug("Copy Silverlight Mscorlib.dll file ");
      FileUtils.copyFileToDirectory(silverlightAssembly, destinationDirectory);
    } catch (IOException e) {
      throw new ExecException("Cannot copy Silverlight 'mscorlib.dll' file to " + destinationDirectory, e);
    }
  }

}
