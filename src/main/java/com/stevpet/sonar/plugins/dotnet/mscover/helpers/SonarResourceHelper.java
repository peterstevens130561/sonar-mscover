package com.stevpet.sonar.plugins.dotnet.mscover.helpers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class SonarResourceHelper {
    private static final Logger LOG = LoggerFactory
            .getLogger(SonarResourceHelper.class);
    /**
     * Gets the resource of the sourcefile, by scanning through the sourcedirs. The
     * path in the resource will  be relative to the project root.
     * @param file to find in project
     * @param 
     * @return resource
     */
    public static org.sonar.api.resources.File getFromFile(File file,Project project) {

        org.sonar.api.resources.File sonarFile =findFileInProject(file, project);
        if (sonarFile == null) {
            LOG.debug("Could not create sonarFile for "
                    + file.getAbsolutePath());
            return null;
        }
        return sonarFile;

    }

    private static  org.sonar.api.resources.File findFileInProject(java.io.File file, Project project) {
        List<File> lf = project.getFileSystem().getSourceDirs();
        if(lf.size()==0) {
           lf.add(new File("."));
        }

        return findFileInSourceDirs(file,lf );
      }

    private static org.sonar.api.resources.File findFileInSourceDirs(java.io.File file, List<java.io.File> sourceDirs) {
        String relativePath = getRelativePath(sourceDirs, file);
        if (relativePath != null) {
            LOG.debug("Relative path {}",relativePath);
          return new org.sonar.api.resources.File(relativePath);
        }
        return null;
      }
    
    @CheckForNull
    public static String getRelativePath(Collection<File> dirs, File file) {
      List<String> stack = Lists.newArrayList();
      File cursor = file;
      while (cursor != null) {
        File parentDir = parentDir(dirs, cursor);
        if (parentDir != null) {
          return Joiner.on("/").join(stack);
        }
        stack.add(0, cursor.getName());
        cursor = cursor.getParentFile();
      }
      return null;
    }


    @CheckForNull
    private static File parentDir(Collection<File> dirs, File cursor) {
      for (File dir : dirs) {
        if (sameDir(dir,cursor)) {
          return dir;
        }
      }
      return null;
    }

    private static boolean sameDir(File dir1, File dir2) {
        try {
        String path1 = dir1.getCanonicalPath();
        String path2 = dir2.getCanonicalPath();
        boolean same=path1.equals(path2);
        LOG.debug("comparing " + path1 + " " + path2 + "->" + same);
        return same;

        } catch ( IOException e ) {
            throw new SonarException(e);
        }
   
    }
}
