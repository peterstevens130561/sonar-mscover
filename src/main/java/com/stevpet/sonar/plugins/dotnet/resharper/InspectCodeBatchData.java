package com.stevpet.sonar.plugins.dotnet.resharper;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class InspectCodeBatchData implements BatchExtension {
    private File report ;

    public File getReport() {
        return report;
    }

    public void setReport(File report) {
        this.report = report;
    }

    public void init() {
        this.report=null;
    }
}
