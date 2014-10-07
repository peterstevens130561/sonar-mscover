package com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

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
            MsCoverProperties propertiesHelper) {
        ResourceFilter filter= new AntPatternResourceFilter();
        String exclusions = propertiesHelper.getExclusions();
        filter.setExclusions(exclusions);
        String inclusions = propertiesHelper.getInclusions();
        filter.setInclusions(inclusions);
        return filter;
    }
    
    /**
     * Create a filter to find the unit test assemblies
     * @param propertiesHelper
     * @return
     */
    public static ResourceFilter createUnitTestAssembliesFilter(MsCoverProperties propertiesHelper) {
        ResourceFilter filter= new AntPatternResourceFilter();
        String inclusions=propertiesHelper.getUnitTestsAssemblies();
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
