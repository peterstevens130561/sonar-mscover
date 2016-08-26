package com.stevpet.sonar.plugins.dotnet.mscover.coveragereader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.vstestcoverageparser.ModuleNameObserver;

public class OpenCoverModuleNameObserver extends ModuleNameObserver {

	private final static Logger LOG = LoggerFactory
			.getLogger(OpenCoverModuleNameObserver.class);
	
    @Override
    public void registerObservers(ObserverRegistrar registrar) {
        registrar.onElement("FullName", this::moduleNameMatcher);
    }

	public void moduleNameMatcher(String value) {
		if (modulesToParse == null || modulesToParse.isEmpty()) {
			return;
		}
		int lastDirSep=value.lastIndexOf("\\");
		String moduleName;
		if(lastDirSep== -1) {
			moduleName=value;
		} else {
			moduleName=value.substring(lastDirSep+1);
		}
		boolean shouldSkip = !modulesToParse.contains(moduleName.toLowerCase());
		if (!shouldSkip) {
			LOG.debug("Module {} will be parsed", value);
		}
		if (shouldSkip) {
			setSkipTillNextElement("Module");
		}
	}


}
