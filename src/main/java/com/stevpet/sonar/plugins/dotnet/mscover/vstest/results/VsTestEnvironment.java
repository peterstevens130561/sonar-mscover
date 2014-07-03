package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class VsTestEnvironment implements BatchExtension {
    private static final Logger LOG = LoggerFactory.getLogger(VsTestEnvironment.class);
    
    private String coverageXmlPath;
    private String resultsXmlPath;
    private boolean testsHaveRun=false;

    private SonarCoverage sonarCoverage;
    public String getXmlCoveragePath() {
        return coverageXmlPath;
    }
    public void setCoverageXmlPath(String xmlCoveragePath) {
        this.coverageXmlPath = xmlCoveragePath;
    }
    public String getXmlResultsPath() {
        return resultsXmlPath;
    }
    public void setResultsXmlPath(String xmlResultsPath) {
        this.resultsXmlPath = xmlResultsPath;
    }
    public void setTestsHaveRun() {
        testsHaveRun=true;      
    }
    public boolean getTestsHaveRun() {
        return testsHaveRun;
    }
    
    public void setSonarCoverage(SonarCoverage sonarCoverage) {
        this.sonarCoverage = sonarCoverage;
        this.setTestsHaveRun();
    }
    
    public SonarCoverage getSonarCoverage() {
        return sonarCoverage;
    }
}
