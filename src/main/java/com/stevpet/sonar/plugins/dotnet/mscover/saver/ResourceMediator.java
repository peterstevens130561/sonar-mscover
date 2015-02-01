package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;

public interface ResourceMediator {

    /**
     * Connect the saver to context, project and registry
     */

    void setDateFilter(DateFilter dateFilter);

    void setResourceFilter(ResourceFilter resourceFilter);

    ResourceSeam getSonarResource(SensorContext sensorContext, Project project,
            File file);

}