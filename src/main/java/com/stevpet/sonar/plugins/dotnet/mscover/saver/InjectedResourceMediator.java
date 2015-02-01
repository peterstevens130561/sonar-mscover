package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.resources.Project;
import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;

public class InjectedResourceMediator implements ResourceMediator, BatchExtension {
    private DefaultResourceMediator resourceMediator = new DefaultResourceMediator();
    public InjectedResourceMediator(TimeMachine timeMachine, MsCoverProperties msCoverProperties) {
        resourceMediator.setDateFilter(DateFilterFactory.createCutOffDateFilter(timeMachine, msCoverProperties));
        resourceMediator.setResourceFilter(ResourceFilterFactory.createAntPatternResourceFilter(msCoverProperties));
    }

    @Override
    public void setDateFilter(DateFilter dateFilter) {
        resourceMediator.setDateFilter(dateFilter);
    }

    @Override
    public void setResourceFilter(ResourceFilter resourceFilter) {
        resourceMediator.setResourceFilter(resourceFilter);
    }

    @Override
    public ResourceSeam getSonarResource(SensorContext sensorContext,
            Project project, File file) {
        return resourceMediator.getSonarResource(sensorContext, project, file);
    }

}
