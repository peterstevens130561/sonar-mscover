package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverFileNamesAndIdObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.opencover.parser.observers.OpenCoverParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.parser.XmlParserSubject;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;

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
		XmlParserSubject parserSubject = new OpenCoverParserSubject();

		OpenCoverMethodObserver methodObserver = new OpenCoverMethodObserver();
		methodObserver.setRegistry(methodToSourceFileIdMap);
		parserSubject.registerObserver(methodObserver);

		OpenCoverFileNamesAndIdObserver sourceFileNamesObserver = new OpenCoverFileNamesAndIdObserver();
		sourceFileNamesObserver.setRegistry(sourceFileNameTable);
		parserSubject.registerObserver(sourceFileNamesObserver);
		parserSubject.parseFile(coverageFile);
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