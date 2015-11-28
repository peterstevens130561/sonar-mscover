package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverCoverageParser;

public class OpenCoverCoverageReader extends CoverageReaderBase implements
		BatchExtension {


	public OpenCoverCoverageReader(MsCoverConfiguration msCoverProperties) {
		super(
				new OpenCoverCoverageParser(msCoverProperties)
				);
	}

}
