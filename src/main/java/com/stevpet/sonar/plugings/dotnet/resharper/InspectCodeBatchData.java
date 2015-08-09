package com.stevpet.sonar.plugings.dotnet.resharper;

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
}
