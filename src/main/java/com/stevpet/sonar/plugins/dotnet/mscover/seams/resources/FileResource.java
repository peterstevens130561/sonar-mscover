package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.File;


public class FileResource implements ResourceSeam {

    private SensorContext sensorContext;
    private File resource;

    FileResource(SensorContext sensorContext,File file) {
        if(file==null) {
            throw new IllegalArgumentException("Programmer error: FileResource invoked with null argument");
        }
        this.resource=file;
    }

    public void saveMetricValue(Metric metric,
            double value) {
        sensorContext.saveMeasure(resource,metric,value);
        
    }
    public void saveMeasure(Measure measure) {
        sensorContext.saveMeasure(resource,measure);
    }
    
    public boolean isIndexed(boolean acceptExcluded) {
        // TODO Auto-generated method stub
        return sensorContext.isIndexed(resource, acceptExcluded);
    }

    public String getLongName() {
        return resource.getLongName();
    }

    public org.sonar.api.resources.File getResource() {
        // TODO Auto-generated method stub
        return resource;
    }

}
