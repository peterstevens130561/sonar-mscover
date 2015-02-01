package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.TimeMachine;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;

public class DefaultResourceMediatorFactory implements ResourceMediatorFactory {


    public ResourceMediator create() {
        return new DefaultResourceMediator();
    }



    public ResourceMediator createWithFilters(
             TimeMachine timeMachine,
            MsCoverProperties msCoverProperties) {
        ResourceMediator resourceMediator = create();
        resourceMediator.setDateFilter(DateFilterFactory.createCutOffDateFilter(timeMachine, msCoverProperties));
        resourceMediator.setResourceFilter(ResourceFilterFactory.createAntPatternResourceFilter(msCoverProperties));
        return resourceMediator;
    }

    public ResourceMediator createWithEmptyFilters() {
        ResourceMediator resourceMediator = create();
        resourceMediator.setResourceFilter(ResourceFilterFactory.createEmptyFilter());
        resourceMediator.setDateFilter(DateFilterFactory.createEmptyDateFilter());
        return resourceMediator;
    }
}
