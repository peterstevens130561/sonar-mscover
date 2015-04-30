package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;


import java.util.List;

/**
 * extension of {@link CoverageParser} to allow parsing of relevant modules onlhy
 */
public interface FilteringCoverageParser  extends CoverageParser{

	/**
	 * Use to set the modules that should be included
	 * @param modules modules (including suffix) to parse
	 * @return
	 */
	FilteringCoverageParser setModulesToParse(List<String> modules);

}