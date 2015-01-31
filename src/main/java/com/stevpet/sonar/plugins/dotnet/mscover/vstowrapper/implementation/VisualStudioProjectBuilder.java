/*
 * Analysis Bootstrapper for Visual Studio Projects
 * Copyright (C) 2014 SonarSource
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
package com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.implementation;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.stevpet.sonar.plugins.dotnet.mscover.vstowrapper.MicrosoftWindowsEnvironment;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.bootstrap.ProjectBuilder;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.SonarException;

import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

public class VisualStudioProjectBuilder extends ProjectBuilder {

  private static final String SONAR_MODULES_PROPERTY_KEY = "sonar.modules";
  private static final Logger LOG = LoggerFactory.getLogger(VisualStudioProjectBuilder.class);

  private final Settings settings;
private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
private Project project;

  public VisualStudioProjectBuilder(Settings settings,MicrosoftWindowsEnvironment microsoftWindowsEnvironment, Project project) {
    this.settings = settings;
    this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
    this.project = project;
  }

  @Override
  public void build(Context context) {
    build(context, new VisualStudioAssemblyLocator(settings));
  }

  public void build(Context context, VisualStudioAssemblyLocator assemblyLocator) {
    ProjectDefinition sonarProject = context.projectReactor().getRoot();

    File solutionFile = getSolutionFile(sonarProject.getBaseDir());
    if (solutionFile == null) {
      LOG.info("No Visual Studio solution file found.");
      return;
    }

    LOG.info("Using the following Visual Studio solution: " + solutionFile.getAbsolutePath());

    if (settings.hasKey(SONAR_MODULES_PROPERTY_KEY)) {
      LOG.warn("The  \"" + SONAR_MODULES_PROPERTY_KEY + "\" is deprecated");
    }

    sonarProject.resetSourceDirs();

    Set<String> skippedProjects = skippedProjectsByNames();
    boolean hasModules = false;

    SimpleVisualStudioSolution currentSolution = new VisualStudioSolutionParser().parse(solutionFile);
    VisualStudioProjectParser projectParser = new VisualStudioProjectParser();
    for (VisualStudioSolutionProject solutionProject : currentSolution.projects()) {
      if (!isSupportedProjectType(solutionProject)) {
        logSkippedProject(solutionProject, "because its project type is unsupported: " + solutionProject.path());
      } else if (skippedProjects.contains(solutionProject.name())) {
        logSkippedProject(solutionProject, "because it is listed in the property \"" + VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS + "\".");
      } else if (isSkippedProjectByPattern(solutionProject.name())) {
        logSkippedProject(solutionProject, "because it matches the property \"" + VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN + "\".");
      } else {
        File projectFile = relativePathFile(solutionFile.getParentFile(), solutionProject.path());
        if (!projectFile.isFile()) {
          LOG.warn("Unable to find the Visual Studio project file " + projectFile.getAbsolutePath());
        } else {
          SimpleVisualStudioProject project = projectParser.parse(projectFile);
          File assembly = assemblyLocator.locateAssembly(solutionProject.name(), projectFile, project);
          if (skipNotBuildProjects() && assembly == null) {
            logSkippedProject(solutionProject, "because it is not built and \"" + VisualStudioPlugin.VISUAL_STUDIO_SKIP_IF_NOT_BUILT + "\" is set.");
          } else if(assembly==null) {
             throw new SonarException("Project not built " + projectFile.getAbsolutePath()) ;
              
          }else {
            hasModules = true;
            currentSolution.addVisualStudioProject(project);
            if(isTestProject(solutionProject.name()) ) {
                currentSolution.addUnitTestVisualStudioProject(project);
                project.setIsTest();
                
            }
            project.setAssembly(assembly);
           buildModule(sonarProject, solutionProject.name(), projectFile, project, assembly, solutionFile);
          }
        }
      }
    }

    Preconditions.checkState(hasModules, "No Visual Studio projects were found.");
    microsoftWindowsEnvironment.setCurrentSolution(currentSolution);
  }


private static void logSkippedProject(VisualStudioSolutionProject solutionProject, String reason) {
    LOG.info("Skipping the project \"" + solutionProject.name() + "\" " + reason);
  }

  private boolean skipNotBuildProjects() {
    return settings.getBoolean(VisualStudioPlugin.VISUAL_STUDIO_SKIP_IF_NOT_BUILT);
  }

  private boolean isSupportedProjectType(VisualStudioSolutionProject project) {
    String path = project.path().toLowerCase();
    return path.endsWith(".csproj") ||
      path.endsWith(".vbproj") ||
      path.endsWith(".vcxproj");
  }

  private void buildModule(ProjectDefinition solutionProject, String projectName, File projectFile, SimpleVisualStudioProject project, @Nullable File assembly, File solutionFile) {

    boolean isTestProject = isTestProject(projectName);
    LOG.info("Adding the Visual Studio " + (isTestProject ? "test " : "") + "project: " + projectName + "... " + projectFile.getAbsolutePath());

    if (isTestProject) {
        
      //solutionProject.addTestDirs(projectFile.getParentFile());
    } else {
      //solutionProject.addSourceDirs(projectFile.getParentFile());
    }

    for (File file : project.getSourceFiles()) {
      if (!file.isFile()) {
        LOG.warn("Cannot find the file " + file.getAbsolutePath() + " of project " + projectName);
      } else if (!isInSourceDir(file, projectFile.getParentFile())) {
        LOG.warn("Skipping the file " + file.getAbsolutePath() + " of project " + projectName + " located outside of the source directory.");
      } else {
        if (isTestProject) {
          solutionProject.addTestFiles(file.getAbsolutePath());
        } else {
          solutionProject.addSourceFiles(file);
          createResource(file,projectFile.getParentFile());
        }
      }
    }

    //forwardModuleProperties(module);
  }



  protected Resource createResource(File file,File sourceDir) {
      List<File> sourceDirs = new ArrayList<File>();
      sourceDirs.add(sourceDir);
      org.sonar.api.resources.File resource = org.sonar.api.resources.File
              .fromIOFile(file, project);

      if (resource == null) {
          LOG.debug("Could not create resource for {}", file.getName());
      } 
      return resource;
  }

 

  private static boolean isInSourceDir(File file, File folder) {
    try {
      return file.getCanonicalPath().replace('\\', '/').startsWith(folder.getCanonicalPath().replace('\\', '/') + "/");
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Nullable
  private File getSolutionFile(File projectBaseDir) {
    File result;

    String solutionPath = settings.getString(VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY);
    if(Strings.nullToEmpty(solutionPath).isEmpty()) {
        solutionPath = settings.getString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SOLUTION_PROPERTY_KEY);    
    }
    if (!Strings.nullToEmpty(solutionPath).isEmpty()) {
      result = new File(projectBaseDir, solutionPath);
    } else {
      Collection<File> solutionFiles = FileUtils.listFiles(projectBaseDir, new String[] {"sln"}, false);
      if (solutionFiles.isEmpty()) {
        result = null;
      } else if (solutionFiles.size() == 1) {
        result = solutionFiles.iterator().next();
      } else {
        throw new SonarException("Found several .sln files in " + projectBaseDir.getAbsolutePath() +
          ". Please set \"" + VisualStudioPlugin.VISUAL_STUDIO_SOLUTION_PROPERTY_KEY + "\" to explicitly tell which one to use.");
      }
    }

    return result;
  }

  private static File relativePathFile(File file, String relativePath) {
    return new File(file, relativePath.replace('\\', '/'));
  }


  @VisibleForTesting
  static String escapeProjectName(String projectName) {
    String escaped = Normalizer.normalize(projectName, Normalizer.Form.NFD);
    escaped = escaped.replaceAll("\\p{M}", "");
    escaped = escaped.replace(' ', '_');
    escaped = escaped.replace('+', '_');
    return escaped;
  }

  private boolean isTestProject(String projectName) {
    return matchesPropertyRegex(VisualStudioPlugin.VISUAL_STUDIO_TEST_PROJECT_PATTERN, projectName);
  }

  private boolean isSkippedProjectByPattern(String projectName) {
    return matchesPropertyRegex(VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN, projectName);
  }

  private boolean matchesPropertyRegex(String propertyKey, String value) {
    String pattern = settings.getString(propertyKey);
    try {
      return pattern != null && value.matches(pattern);
    } catch (PatternSyntaxException e) {
      LOG.error("The syntax of the regular expression of the \"" + propertyKey + "\" property is invalid: " + pattern);
      throw Throwables.propagate(e);
    }
  }

  private Set<String> skippedProjectsByNames() {
    String skippedProjects = settings.getString(VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS);
    if (skippedProjects == null) {
      return ImmutableSet.of();
    }

    LOG.warn("Replace the deprecated property \"" + VisualStudioPlugin.VISUAL_STUDIO_OLD_SKIPPED_PROJECTS + "\" by the new \""
      + VisualStudioPlugin.VISUAL_STUDIO_SKIPPED_PROJECT_PATTERN + "\".");

    return ImmutableSet.<String>builder().addAll(Splitter.on(',').omitEmptyStrings().split(skippedProjects)).build();
  }

}