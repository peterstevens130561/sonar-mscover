package com.stevpet.sonar.plugins.dotnet.mscover.vstest.results;

import java.io.File;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.ProjectSeam;
import com.stevpet.sonar.plugins.dotnet.mscover.seams.SonarProjectSeam;

@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
public class VsTestEnvironment implements BatchExtension {
    private static final Logger LOG = LoggerFactory.getLogger(VsTestEnvironment.class);
    private ProjectSeam projectSeam = new SonarProjectSeam();
    private String coverageXmlPath;
    private String resultsXmlPath;
    private boolean testsHaveRun=false;
    private ModuleFileSystem moduleFileSystem;

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
    public void setTestResultsXmlPath(String xmlResultsPath) {
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
    public void setCoverageXmlFile(Project project, String string) {
        projectSeam.setProject(project);
        File opencoverCoverageFile= projectSeam.getSonarFile("coverage-report.xml");
        String openCoverCoveragePath= opencoverCoverageFile.getAbsolutePath();
        setCoverageXmlPath(openCoverCoveragePath); 
    }

}
