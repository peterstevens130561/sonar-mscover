package com.stevpet.sonar.plugins.dotnet.mscover.sonarseams;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;

/**
 * Use as seam between the app and sonar to save any measure. Allows better
 * testing of the saving, as you can intercept the saving with your own
 * implementation
 * 
 * @author stevpet
 * 
 */
public class SonarMeasureSaver implements MeasureSaver {
    private SensorContext sensorContext;
    private ResourceMediator resourceMediator;
    private Resource resource;
    private Boolean ignoreSaveTwice=false;

    private SonarMeasureSaver(SensorContext sensorContext,
            ResourceMediator resourceMediator) {
        this.sensorContext = sensorContext;
        this.resourceMediator = resourceMediator;
    }

    public static SonarMeasureSaver create(SensorContext sensorContext,
            ResourceMediator resourceMediator) {
        return new SonarMeasureSaver(sensorContext, resourceMediator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver#setFile
     * (java.io.File)
     */
    public void setFile(File file) {
        resource = resourceMediator.getSonarFileResource(file);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver#saveMeasure
     * (org.sonar.api.measures.Measure)
     */
    public void saveFileMeasure(Measure measure) {
        if (resource != null) {
            try {
                sensorContext.saveMeasure(resource, measure);
            } catch (RuntimeException e) {
                if (!ignoreSaveTwice) {
                    throw e;
                }
            }
        }
    }

    public void saveFileMeasure(Metric metric, double value) {
        if (resource != null) {
            try {
                sensorContext.saveMeasure(resource, metric, value);
            } catch (RuntimeException e) {
                if (!ignoreSaveTwice) {
                    throw e;
                }
            }
        }
    }

    public void saveSummaryMeasure(Metric metric, double value) {
        sensorContext.saveMeasure(metric, value);
    }

    public void setIgnoreTwiceSameMeasure() {
        ignoreSaveTwice=true;

    }

    public void setExceptionOnTwiceSameMeasure() {
        ignoreSaveTwice=false;
    }

}
