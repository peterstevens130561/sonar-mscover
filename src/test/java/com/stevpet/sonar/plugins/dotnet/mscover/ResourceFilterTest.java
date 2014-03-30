package com.stevpet.sonar.plugins.dotnet.mscover;

import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.resourcefilter.FileResourceFilter;

import junit.framework.Assert;

public class ResourceFilterTest {

    public void NoFiltersDefined_Passes() {
        //Arrrange
        FileResourceFilter filter = new FileResourceFilter() ;
        Resource resource ;
        
        //Act
        //boolean isIncluded = filter.isIncludedFileResource(resource);
        //Assert
        //Assert.assertTrue(isIncluded);
    }
}
