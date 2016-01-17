package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.dotnet.mscover.MsCoverConfiguration;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.FilteringCoverageParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.ModuleNameObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.sonar.SonarCoverage;

public class OpenCoverFilteringCoverageParser extends OpenCoverCoverageParser implements
		FilteringCoverageParser  {

    private final Logger LOG = LoggerFactory.getLogger(OpenCoverFilteringCoverageParser.class);

	private ModuleNameObserver moduleNameObserver;
	
	public OpenCoverFilteringCoverageParser(MsCoverConfiguration msCoverConfiguration) {
		super(msCoverConfiguration);
		moduleNameObserver= new OpenCoverModuleNameObserver();
		parser.registerObserver( moduleNameObserver);

	}

	@Override
	public void parse(SonarCoverage registry, File file) {
		super.parse(registry, file);
	}

	@Override
	public FilteringCoverageParser setModulesToParse(List<String> modules) {
        moduleNameObserver.addModulesToParse(modules);
        return this;
	}

}
