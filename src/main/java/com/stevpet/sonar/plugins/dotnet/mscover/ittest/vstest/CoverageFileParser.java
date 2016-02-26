package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverFilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class CoverageFileParser implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(CoverageFileParser.class);
    private CoverageParser coverageParser;
    private SonarCoverage sonarCoverage;
    private File coverageFile;
    private SonarCoverage coverageDestination;

    public CoverageFileParser(MsCoverConfiguration msCoverConfiguration) {

        coverageParser = new OpenCoverFilteringCoverageParser(msCoverConfiguration);
    }

    public void setCoverageFile(File coverageFile) {
        this.coverageFile = coverageFile;
    }

    public SonarCoverage getCoverage() {
        return sonarCoverage;
    }

    /**
     * Clean coverage to parse the file into
     * @param sonarCoverage
     */
    public void setCoverage(SonarCoverage sonarCoverage) {
        this.sonarCoverage=sonarCoverage;
    }
    
    /**
     * The parsed file will be merged into this one
     * @param sonarCoverage
     */
    public void setMergeDestination(SonarCoverage sonarCoverage) {
        this.coverageDestination = sonarCoverage;
    }
    
    @Override
    public void run() {
        Preconditions.checkState(sonarCoverage!=null, "sonarCoverage not set");
        Preconditions.checkState(coverageFile!=null,"coverageFile not set");
        Preconditions.checkState(coverageDestination!=null,"coverageDestination");
        LOG.debug("Stared parsing {}",coverageFile.getName());
        coverageParser.parse(sonarCoverage, coverageFile);
        LOG.debug("Completed parsing {}",coverageFile.getName());
        synchronized(coverageDestination) {
            LOG.debug("start merging {}",coverageFile.getName());
            coverageDestination.merge(sonarCoverage);
            LOG.debug("completed merging {}",coverageFile.getName());
        }
    }
}
