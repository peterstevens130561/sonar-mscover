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
package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;


class OpenCoverCoverageModuleSaver implements CoverageModuleSaver {

	
	private ModuleParser moduleParser;
	private final CoverageFileLocator coverageFileLocator;
	OpenCoverCoverageModuleSaver(@Nonnull ModuleParser moduleParser) {
		this.moduleParser = moduleParser;
		this.coverageFileLocator = new DefaultCoverageFileLocator();
	}
	
	
    @Override
    public void save(@Nonnull File coverageRootDir,  @Nonnull String testProjectName,@Nonnull String xmlDoc) {
		Preconditions.checkNotNull(testProjectName,"project not set");
		Preconditions.checkNotNull(coverageRootDir,"root not set");
	      Preconditions.checkNotNull(xmlDoc,"root not set");
	      moduleParser.parse(xmlDoc);
	        if(moduleParser.getSkipped() || moduleParser.isNotCovered()) {
	            return;
	        }
		String moduleName = moduleParser.getModuleName();
		File artifactFile= coverageFileLocator.getFile(coverageRootDir, testProjectName, moduleName);
		createModuleDir(artifactFile);	
		saveModule(artifactFile,xmlDoc);
	}

	private void saveModule(File artifactFile,String xmlDoc) {
		try {
			Writer writer = new FileWriter(artifactFile);
			writer.write(xmlDoc);
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	

	private  File createModuleDir(File artifactFile) {
		File moduleDir=artifactFile.getParentFile();
		if(!moduleDir.exists()) {
		    // multithreading may kick in here
			if(!moduleDir.mkdirs() || !moduleDir.exists()) {
				throw new IllegalStateException("Could not create dir " + moduleDir.getAbsolutePath());
			}
		}
		if(!moduleDir.isDirectory()) {
			throw new IllegalStateException("Is not a directory " + moduleDir.getAbsolutePath());
		}
	    
		return moduleDir;
	}


		

}
