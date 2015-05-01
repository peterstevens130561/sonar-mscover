package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.io.File;
import java.util.List;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;



/**
 * parses VsTest generated coverage files
 * @see FilteringCoverageParser
 *
 */
public class VsTestFilteringCoverageParser implements FilteringCoverageParser {

	private List<String> modules;

	@Override
	public FilteringCoverageParser setModulesToParse(List<String> modules) {
		this.modules = modules;
		return this;
	}

	@Override
	public void parser(SonarCoverage registry,  File file) {
	      ModuleNameObserver moduleNameObserver = new ModuleNameObserver();
	        moduleNameObserver.addModulesToParse(modules);
	        XmlParserSubject parserSubject = new CoverageParserSubject();

	        VsTestCoverageObserver[] observers = {
					new FileNamesObserver(),
					new LinesObserver(),
	                moduleNameObserver
	        };
	        
	        for(VsTestCoverageObserver observer : observers) {
	            observer.setVsTestRegistry(registry);
	            parserSubject.registerObserver(observer);            
	        }
	}

}
