package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

/**
 * Load a coverage file into memory.
 *
 */
public interface CoverageParserStep {

	/**
	 * The coverage parser may  be invoked multiple times during a run
	 * @param registry in which to put to parser results
	 * @param file to parse
	 */
    void parse(SonarCoverage registry, File file);
}
