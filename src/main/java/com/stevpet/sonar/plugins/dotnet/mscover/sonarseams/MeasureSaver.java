package com.stevpet.sonar.plugins.dotnet.mscover.sonarseams;

import java.io.File;

import org.sonar.api.measures.Measure;

import com.stevpet.sonar.plugins.dotnet.mscover.saver.ResourceMediator;

/**
 * Use as seam between the app and sonar to save any measure. Allows better testing of the
 * saving, as you can intercept the saving with your own implementation
 * @author stevpet
 *
 */
public interface MeasureSaver {

    /**
     * set the resource file on which we'll set the measure. The resourcemediator will attempt
     * to translate it to a resource.
     * @param file
     */
    public abstract void setFile(File file);

    /**
     * if the file is to be included in the analysis then the measure will be saved
     * @param measure
     */
    public abstract void saveMeasure(Measure measure);

}