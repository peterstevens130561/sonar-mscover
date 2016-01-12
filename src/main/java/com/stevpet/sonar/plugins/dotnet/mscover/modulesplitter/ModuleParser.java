package com.stevpet.sonar.plugins.dotnet.mscover.modulesplitter;

import java.io.StringWriter;

public interface ModuleParser {

	/**
	 * parses the coverage data in the stringWriter to get the moduleName
	 * @param module - proper coveragefile that has one module
	 */
	void parse(String module);
	String getModuleName();
}
