package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;


import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.File;

public class SonarResourceSeamFactory implements ResourceSeamFactory{

    private SensorContext sensorContext;

    public SonarResourceSeamFactory(SensorContext sensorContext) {
        this.sensorContext = sensorContext;
    }
    public ResourceSeam createFileResource(File resource) {
        return new FileResource(sensorContext,resource);
    }

    public ResourceSeam createNullResource() {

        return new NullResource();
    }

}
