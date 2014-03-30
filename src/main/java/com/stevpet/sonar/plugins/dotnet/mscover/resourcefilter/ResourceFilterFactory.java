package com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter;

import com.stevpet.sonar.plugins.dotnet.mscover.PropertiesHelper;

public class ResourceFilterFactory {

    private ResourceFilterFactory() {
        
    }
    /**
     * Create a filter with inclusions and exclusions set.
     * 
     * @param propertiesHelper to get the settings
     * @return populated filter
     */
    public static ResourceFilter createAntPatternResourceFilter(
            PropertiesHelper propertiesHelper) {
        ResourceFilter filter= new AntPatternResourceFilter();
        String exclusions = propertiesHelper.getExclusions();
        filter.setExclusions(exclusions);
        String inclusions = propertiesHelper.getInclusions();
        filter.setInclusions(inclusions);
        return filter;
    }

    /**
     * create an empty filter, will pass everything
     * 
     * @return
     */
    public static ResourceFilter createEmptyFilter() {
        return new AlwaysPassThroughResourceFilter();
    }

}
