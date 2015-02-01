package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.TimeMachine;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

@Deprecated
public interface ResourceMediatorFactory {
 
    @Deprecated
    ResourceMediator createWithFilters(
            TimeMachine timeMachine,
           MsCoverProperties msCoverProperties);
    @Deprecated
    ResourceMediator createWithEmptyFilters();
    
}
