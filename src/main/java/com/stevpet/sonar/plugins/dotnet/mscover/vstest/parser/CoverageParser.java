package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

/**
 * Parser of coverage files. The parser that implements this should fill the registry
 */
public interface CoverageParser {

	/**
	 * Parse the file, and put coverage information into the registry.
	 * @param registry - empty registry at start, to be filled with line/branch coverage info from file
	 * @param file - coveragefile
	 */
	void parse(SonarCoverage registry, File file);

}