package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

/**
 * Load a coverage file into memory.
 *
 */
public interface CoverageReaderStep {

	/**
	 * The coverage read may  be invoked multiple times during a run
	 * @param registry in which to put to parser results
	 * @param file to parse
	 */
    void read(SonarCoverage registry, File file);
}
