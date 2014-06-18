package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

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
    boolean isIndexed(boolean acceptExcluded);
    String getLongName();
    org.sonar.api.resources.File getResource();
    
}
