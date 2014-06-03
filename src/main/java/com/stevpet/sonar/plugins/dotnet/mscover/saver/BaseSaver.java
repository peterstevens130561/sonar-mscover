package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.scan.filesystem.PathResolver.RelativePath;
import org.sonar.api.utils.SonarException;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;

public abstract class BaseSaver implements Saver {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(BaseSaver.class);

    protected DateFilter dateFilter;
    protected ResourceFilter resourceFilter;
    private Project project ;
    private SensorContext context;
    private Charset charset;

    public BaseSaver(SensorContext context,Project project) {
        this.project = project ;
        this.context = context;
        setCharset(project);
    }

    private void setCharset(Project project) {
        String charsetName;
        if(project==null) {
           charsetName="UTF-8";
        } else {
           charsetName = project.getFileSystem().getSourceCharset().name();
        }
        charset = Charset.forName(charsetName);
    }

    
    /**
     * Connect the saver to context, project and registry
     */
        
    public void setDateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }
    
    public void setResourceFilter(ResourceFilter resourceFilter) {
        this.resourceFilter = resourceFilter;
    }
    

    /**
     * Gets the resource of the file if it is to be included in the
     * analysis. The resource will have been loaded into SonarQube.
     * @param file
     * @return resource
     */
    public org.sonar.api.resources.File getSonarFileResource(File file) {

        org.sonar.api.resources.File sonarFile =fromIOFile(file, project);
        if (sonarFile == null) {
            LOG.debug("Could not create sonarFile for "
                    + file.getAbsolutePath());
            return null;
        }

        String longName = sonarFile.getLongName();
        if(!resourceFilter.isPassed(longName)) {
            return null;
        }
        if (!dateFilter.isResourceIncludedInResults(sonarFile)) {
            LOG.debug("Skipping file of which commit date is before cutoff date " +sonarFile.getLongName());
            return null;
        }
        if (!context.isIndexed(sonarFile, false)) {           
            readSourceIntoSonar(file, sonarFile);
        }
        return sonarFile ;
    }

    private void readSourceIntoSonar(File file,
            org.sonar.api.resources.File sonarFile) {
        try {
            String qualifier=getQualifier();
            sonarFile.setQualifier(qualifier);
            context.index(sonarFile);
              String source = Files.toString(file, charset);
              source = CharMatcher.anyOf("\uFEFF").removeFrom(source); 
              context.saveSource(sonarFile, source);
              LOG.debug("MSCover added file" +sonarFile.getLongName());
          } catch (IOException e) {
              String msg="Unable to read and import the source file : '" + file.getAbsolutePath();
              LOG.error(msg);
            throw new SonarException(msg,e);
          }
        if(!context.isIndexed(sonarFile, false)) {
            String msg="Can't index file" + file.getAbsolutePath();
            LOG.debug(msg);
            //throw new SonarException(msg);
        }
    }


    
    protected String getQualifier() {
        return "FIL";
    }

    public  org.sonar.api.resources.File fromIOFile(java.io.File file, Project project) {
        List<File> lf = project.getFileSystem().getSourceDirs();
        if(lf.size()==0) {
           lf.add(new File("."));
        }
        for(File dir:lf) {
            LOG.debug("MSCover sourcedir {}",dir.getAbsolutePath());
        }
        return fromIOFile(file,lf );
      }

    public org.sonar.api.resources.File fromIOFile(java.io.File file, List<java.io.File> sourceDirs) {
        String relativePath = getRelativePath(sourceDirs, file);
        if (relativePath != null) {
            LOG.debug("Relative path {}",relativePath);
          return new org.sonar.api.resources.File(relativePath);
        }
        return null;
      }
    
    @CheckForNull
    public String getRelativePath(Collection<File> dirs, File file) {
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
    private File parentDir(Collection<File> dirs, File cursor) {
      for (File dir : dirs) {
        if (sameDir(dir,cursor)) {
          return dir;
        }
      }
      return null;
    }
    @CheckForNull
    public String relativePath(File dir, File file) throws IOException {
      List<String> stack = Lists.newArrayList();
      File cursor = file;
      while (cursor != null) {
        if (sameDir(dir,cursor)) {
          return Joiner.on("/").join(stack);
        }
        stack.add(0, cursor.getName());
        cursor = cursor.getParentFile();
      }
      return null;
    }
  
    public boolean sameDir(File dir1, File dir2) {
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
