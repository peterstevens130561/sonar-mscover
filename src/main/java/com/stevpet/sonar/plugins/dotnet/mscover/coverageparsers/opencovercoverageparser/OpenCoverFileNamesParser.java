package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import java.io.File;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesAndIdObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public class OpenCoverFileNamesParser implements FileNamesParser {

	private MethodToSourceFileIdMap methodToSourceFileIdMap;
	private SourceFileNameTable sourceFileNameTable;
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.FileNamesParser#parse(java.io.File, com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap, com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable)
	 */
	@Override
	public void parse(File coverageFile) {
		
		methodToSourceFileIdMap=new MethodToSourceFileIdMap();
		sourceFileNameTable = new SourceFileNameTable();
		XmlParser xmlParser = new DefaultXmlParser();

		OpenCoverMethodObserver methodObserver = new OpenCoverMethodObserver();
		methodObserver.setRegistry(methodToSourceFileIdMap);
		xmlParser.registerObserver(methodObserver);

		OpenCoverFileNamesAndIdObserver sourceFileNamesObserver = new OpenCoverFileNamesAndIdObserver();
		sourceFileNamesObserver.setRegistry(sourceFileNameTable);
		xmlParser.registerObserver(sourceFileNamesObserver);
		xmlParser.parseFile(coverageFile);
	}


	@Override
	public MethodToSourceFileIdMap getMethodToSourceFileIdMap() {
		return methodToSourceFileIdMap;
	}

	@Override
	public SourceFileNameTable getSourceFileNamesTable() {
		return sourceFileNameTable;
	}
}
