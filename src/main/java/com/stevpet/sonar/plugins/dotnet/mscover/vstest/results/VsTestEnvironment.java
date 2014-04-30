package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class VsTestEnvironment implements BatchExtension {
    private static final Logger LOG = LoggerFactory.getLogger(VsTestEnvironment.class);
    
    private String coverageXmlPath;
    private String resultsXmlPath;
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
}
