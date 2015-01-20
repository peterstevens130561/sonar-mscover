package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.File;
import org.sonar.api.utils.SonarException;

import com.google.common.base.CharMatcher;
import com.google.common.io.Files;


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
    
    public boolean isIndexed(boolean acceptExcluded) {
        // TODO Auto-generated method stub
        return sensorContext.isIndexed(resource, acceptExcluded);
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

    public void readSource(java.io.File file,String qualifier,Charset charset) {
        if(isExcluded) {
            return;
        }
        try {
            resource.setQualifier(qualifier);
            sensorContext.index(resource);
            String source = Files.toString(file, charset);
            source = CharMatcher.anyOf("\uFEFF").removeFrom(source); 
            sensorContext.saveSource(resource, source);
          } catch (IOException e) {
              String msg="Unable to read and import the source file : '" + file.getAbsolutePath();
              LOG.error(msg);
            throw new SonarException(msg,e);
          }
    
        if(!sensorContext.isIndexed(resource, false)) {
            String msg="Can't index file" + file.getAbsolutePath();
            LOG.debug(msg);
        }
    }


    public void setIsExcluded() {
        isExcluded=true;
    }

    public void readSource(String path) {
        // TODO Auto-generated method stub
        
    }
}
