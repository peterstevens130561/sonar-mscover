package com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.FilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class IntegrationTestCoverageReaderBase implements
        CoverageReader {
    private final static Logger LOG = LoggerFactory
            .getLogger(IntegrationTestCoverageReaderBase.class);

    private final MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
    private final FilteringCoverageParser coverageParser;
    private  MsCoverConfiguration msCoverConfiguration;
    private final IntegrationTestsConfiguration integrationTestConfiguration;
    private ExecutorService executorService;

    public IntegrationTestCoverageReaderBase(
            MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
            FilteringCoverageParser coverageParser,
            IntegrationTestsConfiguration integrationTestConfiguration) {
        this.microsoftWindowsEnvironment = microsoftWindowsEnvironment;
        this.coverageParser = coverageParser;
        this.integrationTestConfiguration = integrationTestConfiguration;
    }

    /**
     * 
     * @see com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.
     *      IntegrationTestsCoverageReader
     *      #read(com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage)
     */

    @Override
    public void read(@Nonnull SonarCoverage registry,
            @Nonnull File coverageRoot) {

        if (coverageRoot.isDirectory()) {
            readFilesFromDir(registry, coverageRoot);
        } else {
            coverageParser.parse(registry, coverageRoot);
        }
    }

    private void readFilesFromDir(SonarCoverage registry,
            File integrationTestsDir) {

        List<String> artifactNames = microsoftWindowsEnvironment
                .getArtifactNames();
        coverageParser.setModulesToParse(artifactNames);
        Collection<File> coverageFiles = FileUtils.listFiles(integrationTestsDir, new String[] { "xml" }, true);
        int threads = integrationTestConfiguration.getCoverageReaderThreads();
        executorService = Executors.newFixedThreadPool(threads);
        for (File coverageFile : coverageFiles) {
            parseFile(registry, coverageFile);
        }
        try {
            executorService.shutdown();
            int timeout = integrationTestConfiguration.getCoverageReaderTimeout();
            if (!executorService.awaitTermination(timeout, TimeUnit.MINUTES)) {
                throw new SonarException("Timeout occurred during parsing of coveragefiles");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setMsCoverConfiguration(MsCoverConfiguration msCoverConfiguration) {
        this.msCoverConfiguration = msCoverConfiguration;
    }

    public void parseFile(SonarCoverage registry, File coverageFile) {
        CoverageFileParser coverageFileParser = new CoverageFileParser(msCoverConfiguration);
        coverageFileParser.setCoverageFile(coverageFile);
        coverageFileParser.setMergeDestination(registry);
        String threadName = "CoverageFileParser" + coverageFile.getName();
        LOG.debug("Queued " + threadName);
        executorService.submit(coverageFileParser);
    }

}
