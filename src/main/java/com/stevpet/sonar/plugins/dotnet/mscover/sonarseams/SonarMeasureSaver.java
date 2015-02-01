package com.stevpet.sonar.plugins.dotnet.mscover.sonarseams;

import java.io.File;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediatorInterface;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.resources.ResourceSeam;

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
    private ResourceMediatorInterface resourceMediator;
    private ResourceSeam resource;
    private Boolean ignoreSaveTwice=false;
    private Project project;

    private SonarMeasureSaver(Project project,SensorContext sensorContext,
            ResourceMediatorInterface resourceMediator) {
        this.sensorContext = sensorContext;
        this.resourceMediator = resourceMediator;
        this.project = project;
    }

    public static SonarMeasureSaver create(Project project,SensorContext sensorContext,
            ResourceMediatorInterface resourceMediator) {
        return new SonarMeasureSaver(project,sensorContext, resourceMediator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver#setFile
     * (java.io.File)
     */
    public void setFile(File file) {
        resource = resourceMediator.getSonarResource(sensorContext, project, file);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stevpet.sonar.plugins.dotnet.mscover.sonarseams.MeasureSaver#saveMeasure
     * (org.sonar.api.measures.Measure)
     */
    public void saveFileMeasure(Measure measure) {
            try {
                resource.saveMeasure(measure);
            } catch (RuntimeException e) {
                if (!ignoreSaveTwice) {
                    throw e;
                }
            }
    }

    public void saveFileMeasure(Metric metric, double value) {
            try {
                resource.saveMetricValue(metric, value);
            } catch (RuntimeException e) {
                if (!ignoreSaveTwice) {
                    throw e;
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
