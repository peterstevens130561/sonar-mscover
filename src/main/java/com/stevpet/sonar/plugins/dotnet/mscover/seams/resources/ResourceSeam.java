package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import java.io.File;
import java.nio.charset.Charset;

import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;


/**
 * Seam for the sonar resources, specifications should
 * 
 * @author stevpet
 *
 */
public interface ResourceSeam {
    void saveMetricValue(Metric metric, double value);
    void saveMeasure(Measure measure);
    String getLongName();    
    boolean isIncluded();
    void readSource(File file, String path, Charset charset);
    void setIsExcluded();
    
}
