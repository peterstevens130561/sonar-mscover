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
	
    public OpenCoverFileNamesParser(MethodToSourceFileIdMap map, SourceFileNameTable sourceFileNamesTable) {
        this.methodToSourceFileIdMap=map;
        this.sourceFileNameTable=sourceFileNamesTable;
    }
    private  void parse(MethodToSourceFileIdMap methodToSourceFileIdMap, File coverageFile) {
        XmlParser xmlParser = new DefaultXmlParser();

        OpenCoverMethodObserver methodObserver = new OpenCoverMethodObserver();
        methodObserver.setRegistry(methodToSourceFileIdMap);
        xmlParser.registerObserver(methodObserver);

        OpenCoverFileNamesAndIdObserver sourceFileNamesObserver = new OpenCoverFileNamesAndIdObserver();
        sourceFileNamesObserver.setRegistry(sourceFileNameTable);
        xmlParser.registerObserver(sourceFileNamesObserver);
        xmlParser.parseFile(coverageFile);  
    }
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.FileNamesParser#parse(java.io.File, com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap, com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable)
	 */
	@Override
	public void parse(File coverageFile) {
		parse(methodToSourceFileIdMap, coverageFile);
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
