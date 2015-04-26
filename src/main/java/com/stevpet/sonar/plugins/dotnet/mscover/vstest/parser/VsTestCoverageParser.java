package com.stevpet.sonar.plugins.dotnet.mscover.vstest.parser;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.vstest.coverageparser.CoverageParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.workflow.CoverageParserStep;

/**
 * parser for coverage file created by vstest
 */
public class VsTestCoverageParser implements CoverageParserStep {
	
	@Override
	public void parse(SonarCoverage registry, File file) {
		XmlParserSubject parserSubject = new CoverageParserSubject();

		VsTestCoverageObserver[] observers = {
				new FileNamesObserver(),
				new LinesObserver()
		};

		for(VsTestCoverageObserver observer : observers) {
			observer.setVsTestRegistry(registry);
			parserSubject.registerObserver(observer);            
		}
		parserSubject.parseFile(file);
	}
}