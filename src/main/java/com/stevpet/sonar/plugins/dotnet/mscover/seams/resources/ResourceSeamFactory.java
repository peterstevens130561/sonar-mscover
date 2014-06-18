package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;


import org.sonar.api.resources.File;

public interface ResourceSeamFactory {
    ResourceSeam createFileResource(File resource) ;
    ResourceSeam createNullResource();
}
