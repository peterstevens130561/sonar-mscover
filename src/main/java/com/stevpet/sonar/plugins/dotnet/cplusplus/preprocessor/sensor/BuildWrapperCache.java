package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class BuildWrapperCache implements BatchExtension {
    private boolean hasRun;
    private File outputDir;
    public void reset() {
        hasRun=false;
        setOutputDir(null);
    }
    public boolean hasRun() {
        return hasRun;
    }
    public void setHasRun() {
        this.hasRun = true;
    }
    public File getOutputDir() {
        return outputDir;
    }
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }
    
}
