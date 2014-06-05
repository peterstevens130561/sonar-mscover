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
import com.stevpet.sonar.plugins.dotnet.mscover.helpers.SonarResourceHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;

public abstract class BaseSaver implements Saver {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(BaseSaver.class);

    protected DateFilter dateFilter;
    protected ResourceFilter resourceFilter;
    private Project project ;
    private SensorContext context;
    private Charset charset;
    private ResourceMediator resourceMediator;

    public BaseSaver(SensorContext context,Project project) {
        this.project = project ;
        this.context = context;
        setCharset(project);
        resourceMediator=ResourceMediator.create(context,project);
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
        resourceMediator.setDateFilter(dateFilter);
        this.dateFilter = dateFilter;
    }
    
    public void setResourceFilter(ResourceFilter resourceFilter) {
        resourceMediator.setResourceFilter(resourceFilter);
        this.resourceFilter = resourceFilter;
    }
    

    /**
     * Gets the resource of the file if it is to be included in the
     * analysis. The resource will have been loaded into SonarQube.
     * @param file
     * @return resource
     */
    public org.sonar.api.resources.File getSonarFileResource(File file) {
        return resourceMediator.getSonarFileResource(file);
    }
    public org.sonar.api.resources.File getSonarFileResourceOld(File file) {
        
        org.sonar.api.resources.File sonarFile =SonarResourceHelper.getFromFile(file, project);
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
}
