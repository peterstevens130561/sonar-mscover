package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coveragereader.CoverageReader;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

/**
 * reader for coverage file created by vstest
 */
public class CoverageReaderBase implements CoverageReader {
	
	private CoverageParser coverageParser;
	
	@SuppressWarnings("ucd")
	public CoverageReaderBase(CoverageParser parser) {
		this.coverageParser = parser;
	}
	@Override
	public void read(SonarCoverage registry, File file) {
		coverageParser.parse(registry, file);
	}
}