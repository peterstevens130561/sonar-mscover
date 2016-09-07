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
package com.stevpet.sonar.plugins.dotnet.mscover.coveragetoxmlconverter;

import java.io.File;

public interface BinaryCoverageToXmlConverter {


	/**
	 * does the conversion of either file or directory

	 * @param source
	 * If the source is a single file, then it returns the converted file
	 * IF the source is a directory, then it returns that directory.
	 * The conversion will remove the original files
	 * If there are no binary files, but there are xml files, then it will behave is if they are there
	 * If there are no binary files, and no xml files, then null will be returned.
	 */

	File convertFiles(File source);

	void convert(File file, File file2);

}