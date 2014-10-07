package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
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
    private Charset charset;
    private ResourceSeamFactory resourceSeamFactory;

    public ResourceMediator(SensorContext context,Project project) {
        this.project = project ;
        setCharset(project);
        resourceSeamFactory = new SonarResourceSeamFactory(context);
    }
    
    public static ResourceMediator create(SensorContext context,Project project) {
        return new ResourceMediator(context,project);
    }

    public static ResourceMediator createWithFilters(
            SensorContext sensorContext, Project project,TimeMachine timeMachine,
            MsCoverProperties propertiesHelper) {
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

        if (!sonarFile.isIndexed(false)) {
                sonarFile.readSource(file,Qualifiers.UNIT_TEST_FILE,charset);
        }
        return sonarFile ;
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
            resource.setIsExcluded();
        }
        if (!dateFilter.isResourceIncludedInResults(sonarFile)) {
            resource.setIsExcluded();
            LOG.debug("Skipping file of which commit date is before cutoff date " +sonarFile.getLongName());
        }
        return resource;
    }

}
