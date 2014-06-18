package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.utils.SonarException;

import com.google.common.base.CharMatcher;
import com.google.common.io.Files;
import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.helpers.SonarResourceHelper;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeamFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.SonarResourceSeamFactory;

public class ResourceMediator {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(ResourceMediator.class);

    protected DateFilter dateFilter = DateFilterFactory.createEmptyDateFilter();
    protected ResourceFilter resourceFilter = ResourceFilterFactory.createEmptyFilter();
    private Project project ;
    private SensorContext context;
    private Charset charset;
    private ResourceSeamFactory resourceSeamFactory;

    public ResourceMediator(SensorContext context,Project project) {
        this.project = project ;
        this.context = context;
        setCharset(project);
        resourceSeamFactory = new SonarResourceSeamFactory(context);
    }
    
    public static ResourceMediator create(SensorContext context,Project project) {
        return new ResourceMediator(context,project);
    }

    public static ResourceMediator createWithFilters(
            SensorContext sensorContext, Project project,TimeMachine timeMachine,
            PropertiesHelper propertiesHelper) {
        ResourceMediator resourceMediator = create(sensorContext,project);
        resourceMediator.setDateFilter(DateFilterFactory.createCutOffDateFilter(timeMachine, propertiesHelper));
        resourceMediator.setResourceFilter(ResourceFilterFactory.createAntPatternResourceFilter(propertiesHelper));
        return resourceMediator;
    }
    
    public static ResourceMediator createWithEmptyFilters(SensorContext sensorContext, Project project) {
        ResourceMediator resourceMediator = create(sensorContext,project);
        resourceMediator.setResourceFilter(ResourceFilterFactory.createEmptyFilter());
        resourceMediator.setDateFilter(DateFilterFactory.createEmptyDateFilter());
        return resourceMediator;
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
    public ResourceSeam getSonarFileResource(File file) {
        return getSonarResource(file);

    }

    public ResourceSeam getSonarTestResource(File file) {

        ResourceSeam sonarFile = getSonarResource(file);
        if(sonarFile == null) {
            return null ;
        }
        if (sonarFile.isIndexed(false)) {           
            readSourceIntoSonar(file,sonarFile.getResource(),Qualifiers.UNIT_TEST_FILE);
        }
        return sonarFile ;
    }

    private Object getResource() {
        // TODO Auto-generated method stub
        return null;
    }

    private ResourceSeam getSonarResource(File file) {
        ResourceSeam resource;
        org.sonar.api.resources.File sonarFile =SonarResourceHelper.getFromFile(file, project);
        if (sonarFile == null) {
            LOG.debug("Could not create sonarFile for "
                    + file.getAbsolutePath());
            resource=resourceSeamFactory.createNullResource();
        } else {
            resource=resourceSeamFactory.createFileResource(sonarFile);
        }

        String longName = resource.getLongName();
        if(!resourceFilter.isPassed(longName)) {
            return null;
        }
        if (!dateFilter.isResourceIncludedInResults(sonarFile)) {
            LOG.debug("Skipping file of which commit date is before cutoff date " +sonarFile.getLongName());
            return null;
        }
        return resource;
    }
    private void readSourceIntoSonar(File file,
            org.sonar.api.resources.File sonarFile,String qualifier) {
        try {
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
 




}
