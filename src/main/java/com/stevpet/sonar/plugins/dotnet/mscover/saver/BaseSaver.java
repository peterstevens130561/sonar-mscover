package com.stevpet.sonar.plugins.dotnet.mscover.saver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.ParsingUtils;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.ResourceFilter;

public abstract class BaseSaver implements Saver {
    
    private static final Logger LOG = LoggerFactory
            .getLogger(BaseSaver.class);
    protected DateFilter dateFilter;
    protected ResourceFilter resourceFilter;
    


    
    /**
     * Connect the saver to context, project and registry
     */
        
    public void setDateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }
    
    public void setResourceFilter(ResourceFilter resourceFilter) {
        this.resourceFilter = resourceFilter;
    }
    
  




  
}
