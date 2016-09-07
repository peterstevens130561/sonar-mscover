/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
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
             integrationTestConfiguration.getCoverageReaderTimeout();
            if (!executorService.awaitTermination(100, TimeUnit.MINUTES)) {
                throw new IllegalStateException("Timeout occurred during parsing of coveragefiles");
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
