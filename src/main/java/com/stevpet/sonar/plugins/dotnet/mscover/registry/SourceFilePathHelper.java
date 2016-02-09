/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
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
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;


/**
 * 
 * As the coverage file may have been created on a different environment than where the analysis is being run
 * the path above the solution may be different. Moreover, not all files will be relevant for the solution so
 * We need to be able to verify that the module could be in the current solution, and if so, we need to
 * create the path for the module in the solution, so that it's existence can be verified.
 * 
 * To use:
 * - first set the solutionPath
 * - then for each module
 *   - set the modulepath
 *    -check that the module is in the solution by isModuleInSolution
 *    -and if so, call getModuleFullPath, to get the path of the module in the solution. Now you can check for
 *     to existence
 *    
 * @return
 */
public class SourceFilePathHelper {
    private static final Logger LOG = LoggerFactory
            .getLogger(SourceFilePathHelper.class);

      private String projectPath;
      private String projectFolder;
      private String sourceFilePath;
      
      public void setProjectFile(File file) {
          try {
              String canonicalPath=file.getCanonicalPath();
            setProjectPath(canonicalPath);
        } catch (IOException e) {
            throw new SonarException("could not get canonicalPath for " + file.getName(),e);
        }
      }
      public void setProjectPath(String path) {
          projectPath=path.trim().toLowerCase();
          String[] projectFolders = projectPath.split("\\\\");
          projectFolder = projectFolders[projectFolders.length - 1];
      }

      /**
       * Get the translated path of the file
       * @param fullPath filename in the coverage file
       * @return filename on the filesystem
       * @throws IOException
       */
      private String getCanonicalPath(String fullPath) {
          if(StringUtils.isEmpty(fullPath)) {
              return StringUtils.EMPTY;
          }
          setFilePath(fullPath);
          if (!isModuleInSolution()) {
              return StringUtils.EMPTY;
          }
          String solutionPath = getSolutionPath();
          File file = new File(solutionPath);

          if (!file.exists()) {
              return StringUtils.EMPTY;
          }
          try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            String msg="Could not get path for " + fullPath;
            LOG.error(msg);
            throw new SonarException(msg,e);
        }
      }
      
      /**
       * Get the file with canonical path of the file
       * @param fullPath filename in the coverage file
       * @return file with path  being canonical
       * @throws IOException
       */
      public File getCanonicalFile(String fullPath) {
          String path = getCanonicalPath(fullPath);
          if(StringUtils.isEmpty(path) ) {
              return null;
          }
          return new File(path);
          
      }
      /**
       * full path the the source file (from the coverage file)
       * @param path
       */
      public void setFilePath(String path) {
          sourceFilePath = path.trim().toLowerCase();
      }
      public String getSolutionFolder() {
          return projectFolder;
      }
      

      
      /**
       * With solutionFolder and ModulePath defined, get the full path for the file in the solution. If the
       * sourceFile is in the projectFolder, then that is used, otherwise it attempts to find the project in the sourceFile's
       * path, and replace the upper part by the project's path.
       * @return
       */
      public String getSolutionPath() {
          if(isSourceFileInProject()) {
              return sourceFilePath ;
          }
          String coveragePathRelativeToSolution = getRelativePath();
          if(coveragePathRelativeToSolution == null) {
              return null;
          }
          return  projectPath + coveragePathRelativeToSolution;
      }

    private boolean isSourceFileInProject() {
        return sourceFilePath.startsWith(projectPath);
    }

    private String getRelativePath() {
        int solutionIndex = getLastOccurenceOfSolutionFolderInModule();
          if (solutionIndex == -1) {
              return StringUtils.EMPTY;
          }
          int startPosOfLocalFile = solutionIndex + 1
                  + projectFolder.length();
          String coveragePathRelativeToSolution = sourceFilePath
                  .substring(startPosOfLocalFile);
        return coveragePathRelativeToSolution;
    }
      
    
    public boolean isModuleInSolution() {
        int solutionIndex = getLastOccurenceOfSolutionFolderInModule();
        return solutionIndex != -1;
    }
    
    private int getLastOccurenceOfSolutionFolderInModule() {
        int solutionIndex = sourceFilePath
                .lastIndexOf("\\" + projectFolder + "\\");
        return solutionIndex;
    }
  }
