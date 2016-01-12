package com.stevpet.sonar.plugins.dotnet.mscover.modulesplitter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.annotation.Nonnull;

import org.codehaus.plexus.util.FileUtils;
import org.sonar.api.utils.SonarException;

import com.google.common.base.Preconditions;


public class ModuleSaverLambda implements ModuleLambda {

	
	private ModuleParser moduleParser;
	private File root;
	private String projectName;

	public ModuleSaverLambda(@Nonnull ModuleParser moduleParser) {
		this.moduleParser = moduleParser;
	}
	
	@Override 
	public ModuleLambda setProject(@Nonnull String projectName) {
		this.projectName = projectName;
		return this;
	}
	
	@Override
	public ModuleLambda setDirectory(@Nonnull File root) {
		this.root=root;
		return this;
	}
	
	@Override
	public void execute(String xmlDoc)  {
		Preconditions.checkNotNull(projectName,"project not set");
		Preconditions.checkNotNull(root,"root not set");
		
		File moduleDir = createModuleDir(xmlDoc);
		
		saveModule(xmlDoc, moduleDir);
	}

	private void saveModule(String xmlDoc, File moduleDir) {
		File moduleFile = new File(moduleDir,projectName + ".xml");
		try {
			Writer writer = new FileWriter(moduleFile);
			writer.write(xmlDoc);
			writer.close();
		} catch (IOException e) {
			throw new SonarException(e);
		}
	}

	private String removeSuffix(String moduleName) {
		int dotPos=moduleName.lastIndexOf(".");
		String module=moduleName.substring(0, dotPos);
		return module;
	}

	private File createModuleDir(String xmlDoc) {
		moduleParser.parse(xmlDoc);
		
		String moduleName=moduleParser.getModuleName();		
		String module = removeSuffix(moduleName);
		File moduleDir=new File(root,module);
		if(!moduleDir.exists()) {
			if(!moduleDir.mkdir()) {
				throw new SonarException("Could not create dir " + moduleDir.getAbsolutePath());
			}
		}
		if(!moduleDir.isDirectory()) {
			throw new SonarException("Is not a directory " + moduleDir.getAbsolutePath());
		}
		return moduleDir;
	}

}
