package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
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
    private ResourceSeamFactory resourceSeamFactory;

    public ResourceMediator() {
        resourceSeamFactory = new SonarResourceSeamFactory();       
    }
    
    @Deprecated
    public ResourceMediator(SensorContext context,Project project) {
        this.project = project ;
        resourceSeamFactory = new SonarResourceSeamFactory();
    }
    


    @Deprecated
    public ResourceMediator(SensorContext sensorContext) {
        resourceSeamFactory = new SonarResourceSeamFactory();
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
    @Deprecated
    public ResourceSeam getSonarFileResource(File file) {
        return getSonarResource(null,project,file);

    }

    @Deprecated
    public ResourceSeam getSonarTestResource(File file) {

        ResourceSeam sonarFile = getSonarResource(null,project,file);
        return sonarFile ;
    }


    public ResourceSeam getSonarResource(SensorContext sensorContext,Project project,File file) {
        ResourceSeam resource;
        org.sonar.api.resources.File sonarFile =SonarResourceHelper.getFromFile(file, project);
        if (sonarFile == null) {
            LOG.debug("Could not create sonarFile for "
                    + file.getAbsolutePath());
            resource=resourceSeamFactory.createNullResource();
        } else {
            resource=resourceSeamFactory.createFileResource(sensorContext,sonarFile);
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
