package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;


import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.File;

public interface ResourceSeamFactory {
    ResourceSeam createFileResource(SensorContext sensorContext,File resource) ;
    ResourceSeam createNullResource();
}
