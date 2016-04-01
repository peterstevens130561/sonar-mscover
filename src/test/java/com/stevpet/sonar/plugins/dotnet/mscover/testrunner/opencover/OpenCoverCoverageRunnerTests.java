package com.stevpet.sonar.plugins.dotnet.mscover.testrunner.opencover;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.results.VsTestEnvironment;
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
