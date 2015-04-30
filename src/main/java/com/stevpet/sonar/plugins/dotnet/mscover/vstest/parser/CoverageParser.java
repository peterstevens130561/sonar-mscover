package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

/**
 * Parser of coverage files. The parser that implements this should fill the registry
 */
public interface CoverageParser {

	void parser(SonarCoverage registry, File file);

}