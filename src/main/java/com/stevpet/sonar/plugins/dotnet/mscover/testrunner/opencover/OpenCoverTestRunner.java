/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
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
 */
package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.stevpet.sonar.plugins.dotnet.mscover.testrunner.TestRunner;

public interface OpenCoverTestRunner extends TestRunner {

	/**
	 * only the assemblies of this solution will be reported in the coverage file
	 * @return 
	 */
	OpenCoverTestRunner onlyReportAssembliesOfTheSolution();

	void setTestCaseFilter(String testCaseFilter);

	/**
	 * only test projects that match this pattern should be included in the test run.
	 * @param string
	 */
    void setTestProjectPattern(@Nonnull Pattern string);

    /**
     * set timeout in minutes
     * @param timeout
     */
    void setTimeout(int timeout);

    /**
     * 
     * @param retries  number of retries > 0
     * @return
     */
    OpenCoverTestRunner setRetries(int retries);


}
