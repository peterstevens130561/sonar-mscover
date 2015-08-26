package com.stevpet.sonar.plugins.dotnet.mscover.workflow.sensor;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class IntegrationTestCache implements BatchExtension {
    private boolean hasRun;
    private SonarCoverage coverage ;

    public SonarCoverage getCoverage() {
        return coverage;
    }

    public void setCoverage(SonarCoverage coverage) {
        this.coverage = coverage;
    }

    public boolean didRun() {
        return hasRun;
    }

    public void hasRun() {
        hasRun=true;
    }
    
}
