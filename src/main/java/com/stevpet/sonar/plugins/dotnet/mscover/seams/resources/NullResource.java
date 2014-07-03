package com.stevpet.sonar.plugins.dotnet.mscover.seams.resources;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;

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


    public boolean isIncluded() {
        return false;
    }



    public void setIsExcluded() {      
    }

    public void readSource(java.io.File file, String path, Charset charset) {

    }
}
