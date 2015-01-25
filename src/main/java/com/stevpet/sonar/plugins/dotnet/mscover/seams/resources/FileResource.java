package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.File;


public class FileResource implements ResourceSeam {
    private static final Logger LOG = LoggerFactory
            .getLogger(FileResource.class);
    private SensorContext sensorContext;
    private File resource;
    private boolean isExcluded = false;
    FileResource(SensorContext sensorContext,File file) {
        if(file==null || sensorContext==null) {
            throw new IllegalArgumentException("Programmer error: FileResource invoked with null argument");
        }
        this.resource=file;
        this.sensorContext=sensorContext;
    }

    public void saveMetricValue(Metric metric,
            double value) {
        sensorContext.saveMeasure(resource,metric,value);
        
    }
    public void saveMeasure(Measure measure) {
        sensorContext.saveMeasure(resource,measure);
    }
    

    public String getLongName() {
        return resource.getLongName();
    }

    public org.sonar.api.resources.File getResource() {
        return resource;
    }
    
    public boolean isIncluded() {
        return !isExcluded;
    }

   
    public void setIsExcluded() {
        isExcluded=true;
    }

}
