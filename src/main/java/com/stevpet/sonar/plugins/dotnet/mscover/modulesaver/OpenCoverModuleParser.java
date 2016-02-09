package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stevpet.sonar.plugins.common.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverParserSubject;

class OpenCoverModuleParser implements ModuleParser{
    private final Logger LOG = LoggerFactory.getLogger(OpenCoverModuleParser.class);
	private XmlParserSubject parser;
	private ModuleFullNameObserver openCoverFullNameObserver;

    public OpenCoverModuleParser() {
    	parser = new OpenCoverParserSubject();
        openCoverFullNameObserver = new OpenCoverFullNameObserver();
        parser.registerObserver(openCoverFullNameObserver);
    }

    public void parse(String xmlDoc) {
        parser.parseString(xmlDoc); 
    }
    
    public String getModuleName() {
    	return openCoverFullNameObserver.getModuleName();   
    }
}

