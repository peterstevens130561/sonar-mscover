package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser;

import java.util.ArrayList;
import java.util.List;

import com.stevpet.sonar.plugins.common.api.parser.BaseParserObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public abstract class ModuleNameObserver extends BaseParserObserver {
    protected List<String> modulesToParse = new ArrayList<String>();
	/**
	 * modules with name (including the .dll, or .exe part) in the list will be
	 * parsed, all others will be ignored
	 * 
	 * @param modules
	 *            modules to parse
	 */
	public void addModulesToParse(List<String> modules) {
        if (modules == null) {
            return;
        }
        for (String module : modules) {

			modulesToParse.add(module.toLowerCase());
        }
    }
}