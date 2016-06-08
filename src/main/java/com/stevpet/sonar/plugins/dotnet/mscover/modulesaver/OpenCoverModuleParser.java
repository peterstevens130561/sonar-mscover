package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverParserSubject;

class OpenCoverModuleParser implements ModuleParser{
    private final Logger LOG = LoggerFactory.getLogger(OpenCoverModuleParser.class);
	private XmlParserSubject parser;
	private ModuleFullNameObserver openCoverFullNameObserver;
	private ModuleSummaryObserver moduleSummaryObserver;
	
    public OpenCoverModuleParser() {
    	parser = new OpenCoverParserSubject();
        openCoverFullNameObserver = new OpenCoverFullNameObserver();
        parser.registerObserver(openCoverFullNameObserver);
        moduleSummaryObserver = new OpenCoverModuleSummaryObserver();
        parser.registerObserver(moduleSummaryObserver);
    }

    public void parse(String xmlDoc) {
        parser.parseString(xmlDoc); 
    }
    
    @Override
    public String getModuleName() {
    	return openCoverFullNameObserver.getModuleName();   
    }

    /**
     * @see com.stevpet.sonar.plugins.dotnet.mscover.modulesaver.ModuleFullNameObserver.getSkipped
     * @return
     */
    @Override
    public boolean getSkipped() {
        return openCoverFullNameObserver.getSkipped();
    }

    @Override
    public boolean isNotCovered() {
        return moduleSummaryObserver.isNotCovered();
    }
}

