package com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder;

import java.io.File;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public interface FileNamesParser {


	/**
	 * parse a coverage file to get {@link MethodToSourceFileIdMap} and {@link SourceFileNameTable}
	 * @param coverageFile
	 */
	void parse(File coverageFile);

	/**
	 * after {@link parse} invoke this to get the map {@link MethodToSourceFileIdMap}
	 * 
	 * @return
	 */
	MethodToSourceFileIdMap getMethodToSourceFileIdMap();

	/**
	 * after {@link parse} invoke to get the {@link SourceFileNameTable}
	 * @return
	 */
	SourceFileNameTable getSourceFileNamesTable();

}