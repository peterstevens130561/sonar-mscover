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
package com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers;

import java.io.File;

import org.sonar.api.BatchExtension;

import com.stevpet.sonar.plugins.dotnet.mscover.model.SourceFileNameTable;
import com.stevpet.sonar.plugins.dotnet.mscover.registry.MethodToSourceFileIdMap;

public interface FileNamesParser extends BatchExtension {


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