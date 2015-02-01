package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilterFactory;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilterFactory;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class DefaultResourceMediator extends ResourceMediator implements BatchExtension {

    public DefaultResourceMediator(TimeMachine timeMachine, MsCoverProperties msCoverProperties,
            FileSystem fileSystem) {
        super();
        this.setDateFilter(DateFilterFactory.createCutOffDateFilter(timeMachine, msCoverProperties));
        this.setResourceFilter(ResourceFilterFactory.createAntPatternResourceFilter(msCoverProperties));
    }

}
