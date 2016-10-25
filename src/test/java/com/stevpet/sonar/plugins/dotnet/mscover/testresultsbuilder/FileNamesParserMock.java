/*
 * Sonar Integration Test Plugin MSCover
 * Copyright (C) 2009 ${owner}
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
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
