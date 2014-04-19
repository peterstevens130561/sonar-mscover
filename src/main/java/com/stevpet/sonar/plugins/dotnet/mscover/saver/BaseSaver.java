package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.api.utils.SonarException;

import com.google.common.base.CharMatcher;
import com.google.common.io.Files;
import com.stevpet.sonar.plugins.dotnet.mscover.blocksaver.BaseBlockSaver;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNamesRegistry;
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
            sonarFile.setQualifier("UTS");
            context.index(sonarFile);
              String source = Files.toString(file, charset);
              source = CharMatcher.anyOf("\uFEFF").removeFrom(source); 
              context.saveSource(sonarFile, source);
              LOG.debug("MSCover added file" +sonarFile.getLongName());
          } catch (Exception e) {
            throw new SonarException("Unable to read and import the source file : '" + file.getAbsolutePath());
          }
        if(!context.isIndexed(sonarFile, false)) {
            throw new SonarException("Can't index file" + file.getAbsolutePath());
        }
    }
    
    public static org.sonar.api.resources.File fromIOFile(java.io.File file, Project project) {
        List<File> lf = project.getFileSystem().getSourceDirs();
        if(lf.size()==0) {
            lf.add(new File("."));
        }
        return fromIOFile(file,lf );
      }

    public static org.sonar.api.resources.File fromIOFile(java.io.File file, List<java.io.File> sourceDirs) {
        PathResolver.RelativePath relativePath = new PathResolver().relativePath(sourceDirs, file);
        if (relativePath != null) {
          return new org.sonar.api.resources.File(relativePath.path());
        }
        return null;
      }
  
}
