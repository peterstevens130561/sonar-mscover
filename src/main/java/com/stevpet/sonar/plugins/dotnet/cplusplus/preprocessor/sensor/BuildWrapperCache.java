package com.stevpet.sonar.plugins.dotnet.cplusplus.preprocessor.sensor;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH) class BuildWrapperCache implements BatchExtension {
    private boolean hasRun;
    private File outputDir;
    
    boolean hasRun() {
        return hasRun;
    }
    void setHasRun() {
        this.hasRun = true;
    }
    public File getOutputDir() {
        return outputDir;
    }
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }
    
}
