package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import org.sonar.api.BatchExtension;
import org.sonar.api.batch.fs.FileSystem;

import com.stevpet.sonar.plugins.dotnet.mscover.IntegrationTestsConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.ittest.vstest.IntegrationTestCoverageReaderBase;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.MicrosoftWindowsEnvironment;

public class OpenCoverIntegrationTestCoverageReader extends
		IntegrationTestCoverageReaderBase implements BatchExtension {

	public OpenCoverIntegrationTestCoverageReader(
			MicrosoftWindowsEnvironment microsoftWindowsEnvironment,
			MsCoverConfiguration msCoverConfiguration,
			FileSystem fileSystem, IntegrationTestsConfiguration integrationTestConfiguration) {
		super(microsoftWindowsEnvironment, 
				new OpenCoverFilteringCoverageParser(msCoverConfiguration),integrationTestConfiguration);
	}

}
