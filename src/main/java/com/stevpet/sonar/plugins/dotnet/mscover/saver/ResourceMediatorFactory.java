package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.sonar.api.batch.TimeMachine;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public interface ResourceMediatorFactory {
 
    
    ResourceMediator createWithFilters(
            TimeMachine timeMachine,
           MsCoverProperties msCoverProperties);
    
    ResourceMediator createWithEmptyFilters();
    
}
