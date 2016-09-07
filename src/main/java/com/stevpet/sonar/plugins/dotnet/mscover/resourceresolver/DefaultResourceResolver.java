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
package com.stevpet.sonar.plugins.dotnet.mscover.resourceresolver;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.File;
import org.sonar.api.scan.filesystem.PathResolver;

/**
 * Wrapper for the fromIOFile statuc
 * @author stevpet
 *
 */
public class DefaultResourceResolver implements ResourceResolver {
	
	private final PathResolver pathResolver ;
	private final FileSystem fileSystem ;
	
	@SuppressWarnings("ucd")
	public DefaultResourceResolver(PathResolver pathResolver,FileSystem fileSystem) {
		this.pathResolver = pathResolver;
		this.fileSystem=fileSystem;
	}
	
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.workflow.ResourceResolver#getFile(java.io.File)
	 */
	@Override
	public File getFile(java.io.File file) {
		File sonarFile=null;
		String relativePathFromBasedir = pathResolver.relativePath(fileSystem.baseDir(), file);
	    if (relativePathFromBasedir != null) {
	        sonarFile= File.create(relativePathFromBasedir);
	    }
	    return sonarFile;
	}
	
	
}
