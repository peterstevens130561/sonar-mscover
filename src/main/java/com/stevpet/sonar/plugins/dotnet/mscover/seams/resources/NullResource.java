package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.File;

public class NullResource implements ResourceSeam {

    public void saveMetricValue(Metric metric, double value) {        
    }

    public void saveMeasure(Measure measure) {      
    }

    /**
     * isIndex may be used to load the file, which we want to prevent, so
     * always return true
     */
    public boolean isIndexed(boolean acceptExcluded) {
        return true;
    }

    public String getLongName() {
        return StringUtils.EMPTY;
    }

    public File getResource() {
        return null;
    }

}
