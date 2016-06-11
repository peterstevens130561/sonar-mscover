package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Preconditions;


class OpenCoverCoverageModuleSaver implements CoverageModuleSaver {

	
	private ModuleParser moduleParser;
	private File root;
	private String projectName;
	private final CoverageFileLocator coverageFileLocator;
	OpenCoverCoverageModuleSaver(@Nonnull ModuleParser moduleParser) {
		this.moduleParser = moduleParser;
		this.coverageFileLocator = new DefaultCoverageFileLocator();
	}
	
	@Override 
	public CoverageModuleSaver setProject(@Nonnull String projectName) {
		this.projectName = projectName;
		return this;
	}
	
	@Override
	public CoverageModuleSaver setDirectory(@Nonnull File root) {
		this.root=root;
		return this;
	}
	
	@Override
	public void save(String xmlDoc)  {
		Preconditions.checkNotNull(projectName,"project not set");
		Preconditions.checkNotNull(root,"root not set");
	      moduleParser.parse(xmlDoc);
	        if(moduleParser.getSkipped() || moduleParser.isNotCovered()) {
	            return;
	        }
		String moduleName = moduleParser.getModuleName();
		File artifactFile= coverageFileLocator.getFile(root, projectName, moduleName);
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
