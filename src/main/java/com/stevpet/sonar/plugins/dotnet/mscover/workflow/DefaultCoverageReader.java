package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.CoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser.VsTestCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageReader;

/**
 * reader for coverage file created by vstest
 */
public class DefaultCoverageReader implements CoverageReader {
	
	private CoverageParser coverageParser;
	
	public DefaultCoverageReader(CoverageParser parser) {
		this.coverageParser = parser;
	}
	@Override
	public void read(SonarCoverage registry, File file) {
		CoverageParser parser = new VsTestCoverageParser();
		parser.parse(registry, file);
	}
}