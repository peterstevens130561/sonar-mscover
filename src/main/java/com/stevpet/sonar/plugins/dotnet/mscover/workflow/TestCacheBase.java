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
package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import com.google.common.base.Preconditions;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.DefaultProjectCoverageRepository;

public abstract class TestCacheBase implements TestCache {

	private File testResults;
	private File testCoverage;
	private boolean hasRun;
	private DefaultProjectCoverageRepository sonarCoverage = null;

	public TestCacheBase() {
		super();
	}

	@Override
	public File getTestResultsFile() {
	    return testResults;
	}

	@Override
	public File getTestCoverageFile() {
	    return testCoverage;
	}

	@Override
	public boolean gatHasRun() {
	    return hasRun;
	}

	@Override public void setHasRun(boolean b) {
		hasRun=b;
	}
	@Override
	public void setHasRun(File coverageFile, File testResultsFile) {
	    this.testCoverage=coverageFile;
	    this.testResults=testResultsFile;
	    hasRun=true;
	}

	@Override
	public TestCache setSonarCoverage(DefaultProjectCoverageRepository sonarCoverage) {
		this.sonarCoverage=sonarCoverage ;
		return this;
	}

	@Override
	public DefaultProjectCoverageRepository getSonarCoverage() {
		Preconditions.checkState(sonarCoverage != null,"sonarCoverage not set");
		return sonarCoverage;
	}

}