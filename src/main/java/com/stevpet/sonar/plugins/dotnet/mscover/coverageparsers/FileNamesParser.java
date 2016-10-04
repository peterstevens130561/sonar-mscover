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
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers;

import java.io.File;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.MethodRepository;
import com.stevpet.sonar.plugins.dotnet.mscover.repositories.SourceFileRepository;

public interface FileNamesParser extends BatchExtension {


	/**
	 * parse a coverage file to get {@link MethodToSourceFileIdRepository} and {@link SourceFileRepository}
	 * @param coverageFile
	 */
	void parse(File coverageFile);

	/**
	 * after {@link parse} invoke this to get the map {@link MethodToSourceFileIdRepository}
	 * 
	 * @return
	 */
	MethodRepository getMethodToSourceFileIdMap();

	/**
	 * after {@link parse} invoke to get the {@link SourceFileRepository}
	 * @return
	 */
	SourceFileRepository getSourceFileRepository();

}