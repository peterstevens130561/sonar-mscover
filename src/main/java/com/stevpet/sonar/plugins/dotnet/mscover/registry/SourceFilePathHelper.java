package com.stevpet.sonar.plugins.dotnet.mscover.registry;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;

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
class SourceFilePathHelper {

      public SourceFilePathHelper() {
          
      }
      private String projectPath;
      private String projectFolder;
      private String sourceFilePath;
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
      public String getCanonicalFile(String fullPath) throws IOException {
          setFilePath(fullPath);
          if (!isModuleInSolution()) {
              return StringUtils.EMPTY;
          }
          String projectPath = getSolutionPath();
          File file = new File(projectPath);

          if (!file.exists()) {
              return StringUtils.EMPTY;
          }
          return file.getCanonicalPath();
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
              Log.info("In project " + sourceFilePath);
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
