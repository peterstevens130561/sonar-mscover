package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
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
    
    public org.sonar.api.resources.File getSonarFileResource(File file) {
        org.sonar.api.resources.File sonarFile = org.sonar.api.resources.File
                .fromIOFile(file, project);
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
           
            try {
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
        return sonarFile ;
    }


  
}
