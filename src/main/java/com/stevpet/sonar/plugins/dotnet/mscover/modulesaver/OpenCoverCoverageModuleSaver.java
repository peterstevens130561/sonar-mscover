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
