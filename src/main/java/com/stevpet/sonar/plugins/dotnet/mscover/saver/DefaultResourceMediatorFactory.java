package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.TimeMachine;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;

public class DefaultResourceMediatorFactory implements ResourceMediatorFactory {


    public ResourceMediatorInterface create() {
        return new ResourceMediator();
    }



    public ResourceMediatorInterface createWithFilters(
             TimeMachine timeMachine,
            MsCoverProperties msCoverProperties) {
        ResourceMediatorInterface resourceMediator = create();
        resourceMediator.setDateFilter(DateFilterFactory.createCutOffDateFilter(timeMachine, msCoverProperties));
        resourceMediator.setResourceFilter(ResourceFilterFactory.createAntPatternResourceFilter(msCoverProperties));
        return resourceMediator;
    }

    public ResourceMediatorInterface createWithEmptyFilters() {
        ResourceMediatorInterface resourceMediator = create();
        resourceMediator.setResourceFilter(ResourceFilterFactory.createEmptyFilter());
        resourceMediator.setDateFilter(DateFilterFactory.createEmptyDateFilter());
        return resourceMediator;
    }
}
