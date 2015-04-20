package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;


import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable;
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