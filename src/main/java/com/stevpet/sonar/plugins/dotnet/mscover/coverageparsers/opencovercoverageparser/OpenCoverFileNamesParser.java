/*******************************************************************************
 * SonarQube MsCover Plugin
 * Copyright (C) 2016 Baker Hughes
 * peter.stevens@bakerhughes.com
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
 *
 * Author: Peter Stevens, peter.stevens@bakerhughes.com
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser;

import java.io.File;

import com.stevpet.sonar.plugins.common.api.parser.XmlParser;
import com.stevpet.sonar.plugins.common.parser.DefaultXmlParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.FileNamesParser;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverFileNamesAndIdObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverMethodObserver;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.impl.DefaultSourceFileRepository;

public class OpenCoverFileNamesParser implements FileNamesParser {

	private MethodToSourceFileIdRepository methodToSourceFileIdMap;
	private SourceFileRepository sourceFileRepository;
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.testresultsbuilder.FileNamesParser#parse(java.io.File, com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdRepository, com.stevpet.sonar.plugins.dotnet.mscover.registry.SourceFileNameTable)
	 */
	@Override
	public void parse(File coverageFile) {
		
		methodToSourceFileIdMap=new MethodToSourceFileIdRepository();
		sourceFileRepository = new DefaultSourceFileRepository();
		XmlParser xmlParser = new DefaultXmlParser();

		OpenCoverMethodObserver methodObserver = new OpenCoverMethodObserver();
		methodObserver.setRegistry(methodToSourceFileIdMap);
		xmlParser.registerObserver(methodObserver);

		OpenCoverFileNamesAndIdObserver sourceFileNamesObserver = new OpenCoverFileNamesAndIdObserver();
		sourceFileNamesObserver.setRegistry(sourceFileRepository);
		xmlParser.registerObserver(sourceFileNamesObserver);
		xmlParser.parseFile(coverageFile);
	}


	@Override
	public MethodToSourceFileIdRepository getMethodToSourceFileIdMap() {
		return methodToSourceFileIdMap;
	}

	@Override
	public SourceFileRepository getSourceFileRepository() {
		return sourceFileRepository;
	}
}
