package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;


import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

import static org.mockito.Mockito.when;

public class FileNamesParserMock extends GenericClassMock<FileNamesParser> {

	public FileNamesParserMock() {
		super(FileNamesParser.class);

	}



	public void givenGetMethodToSourceFileIdMap(
			MethodToSourceFileIdMap methodToSourceFileIdMap) {
		when(instance.getMethodToSourceFileIdMap()).thenReturn(methodToSourceFileIdMap);
		
	}


	public void givenSourceFileNamesTable(
			SourceFileNameTable sourceFileNamesTable) {
		when(instance.getSourceFileNamesTable()).thenReturn(sourceFileNamesTable);
	}

}
