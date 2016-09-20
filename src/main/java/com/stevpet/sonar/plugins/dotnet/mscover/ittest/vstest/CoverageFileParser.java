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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.OpenCoverFilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.ProjectCoverageRepository;

public class CoverageFileParser implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(CoverageFileParser.class);
    private CoverageParser coverageParser;
    private ProjectCoverageRepository sonarCoverage;
    private File coverageFile;
    private ProjectCoverageRepository coverageDestination;

    public CoverageFileParser(MsCoverConfiguration msCoverConfiguration) {
        coverageParser = new OpenCoverFilteringCoverageParser(msCoverConfiguration);
    }

    public void setCoverageFile(File coverageFile) {
        this.coverageFile = coverageFile;
    }

    /**
     * The parsed file will be merged into this one
     * @param sonarCoverage
     */
    public void setMergeDestination(ProjectCoverageRepository sonarCoverage) {
        this.coverageDestination = sonarCoverage;
    }
    
    @Override
    public void run() {
        sonarCoverage=new DefaultProjectCoverageRepository();
        Preconditions.checkState(coverageFile!=null,"coverageFile not set");
        Preconditions.checkState(coverageDestination!=null,"coverageDestination");
        LOG.debug("Started parsing {}",coverageFile.getName());
        coverageParser.parse(sonarCoverage, coverageFile);
        LOG.debug("Completed parsing {}",coverageFile.getName());
        synchronized(coverageDestination) {
            LOG.debug("start merging {}",coverageFile.getName());
            coverageDestination.mergeIntoThis(sonarCoverage);
            LOG.debug("completed merging {}",coverageFile.getName());
        }
    }
}
