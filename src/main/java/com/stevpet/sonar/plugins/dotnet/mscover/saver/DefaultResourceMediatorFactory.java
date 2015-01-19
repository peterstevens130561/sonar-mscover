package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;

public class DefaultResourceMediatorFactory implements ResourceMediatorFactory {

    public ResourceMediator create(SensorContext sensorContext, Project project,FileSystem fileSystem) {
        return new ResourceMediator(sensorContext,project,fileSystem);
    }

    public ResourceMediator createWithFilters(SensorContext sensorContext,
            Project project, TimeMachine timeMachine,
            MsCoverProperties msCoverProperties,FileSystem fileSystem) {
        ResourceMediator resourceMediator = create(sensorContext,project,fileSystem);
        resourceMediator.setDateFilter(DateFilterFactory.createCutOffDateFilter(timeMachine, msCoverProperties));
        resourceMediator.setResourceFilter(ResourceFilterFactory.createAntPatternResourceFilter(msCoverProperties));
        return resourceMediator;
    }

    public ResourceMediator createWithEmptyFilters(SensorContext sensorContext, Project project,FileSystem fileSystem) {
        ResourceMediator resourceMediator = create(sensorContext,project,fileSystem);
        resourceMediator.setResourceFilter(ResourceFilterFactory.createEmptyFilter());
        resourceMediator.setDateFilter(DateFilterFactory.createEmptyDateFilter());
        return resourceMediator;
    }

}
