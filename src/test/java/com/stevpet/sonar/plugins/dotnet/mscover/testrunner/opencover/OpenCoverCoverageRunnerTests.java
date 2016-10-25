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

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class OpenCoverCoverageRunnerTests {

	
	@Mock private MsCoverConfiguration msCoverConfiguration;
	@Mock private MicrosoftWindowsEnvironment microsoftWindowsEnvironment;
	@Mock private FileSystem fileSystem;
	@Mock private Settings settings;

	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
	}
	@Test
	public void instantation() {
		try {
			
			DefaultOpenCoverTestRunner.create(
				msCoverConfiguration, settings,
				microsoftWindowsEnvironment,
				fileSystem);
		} catch (Exception e) {
			fail("could not instantiate, probably because one of the underlying constructors uses a dependency");
		}
	}
}
