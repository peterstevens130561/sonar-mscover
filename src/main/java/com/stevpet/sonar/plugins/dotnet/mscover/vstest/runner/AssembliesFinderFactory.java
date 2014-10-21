package com.stevpet.sonar.plugins.dotnet.mscover.vstest.runner;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverProperties;

public class AssembliesFinderFactory implements AbstractAssembliesFinderFactory {
    public  AssembliesFinder create(MsCoverProperties propertiesHelper) {
        AssembliesFinder finder;
        if(propertiesHelper!=null && propertiesHelper.isIgnoreMissingUnitTestAssembliesSpecified()) {
            finder=new IgnoreMissingAssembliesFinder(propertiesHelper) ;
        } else {
            finder=new DefaultAssembliesFinder(propertiesHelper) ;
        }
        return finder;
    }
}
