package com.stevpet.sonar.plugins.dotnet.mscover.helpers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.annotation.CheckForNull;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class SonarResourceHelper {
    
    private SonarResourceHelper() {
        
    }
    /**
     * Gets the resource of the sourcefile 
     * path in the resource will  be relative to the project root.
     * @param file to find in project
     * @param 
     * @return resource
     */
    public static org.sonar.api.resources.File getFromFile(File file,Project project) {
        return  org.sonar.api.resources.File.fromIOFile(file, project);
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
        return same;

        } catch ( IOException e ) {
            throw new SonarException(e);
        }
   
    }
}
