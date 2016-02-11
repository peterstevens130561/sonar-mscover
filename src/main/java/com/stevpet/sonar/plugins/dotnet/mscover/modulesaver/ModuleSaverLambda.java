package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.sonar.api.utils.SonarException;

import com.google.common.base.Preconditions;


class ModuleSaverLambda implements ModuleLambda {

	
	private ModuleParser moduleParser;
	private File root;
	private String projectName;

	ModuleSaverLambda(@Nonnull ModuleParser moduleParser) {
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
		String moduleName = getArtifactNameFromXmlDoc(xmlDoc);		
		File artifactFile=getArtifactCoverageFile(moduleName);
		createModuleDir(artifactFile);	
		saveModule(artifactFile,xmlDoc);
	}

	private String getArtifactNameFromXmlDoc(String xmlDoc) {
		moduleParser.parse(xmlDoc);
		String moduleName=moduleParser.getModuleName();
		return moduleName;
	}

	private void saveModule(File artifactFile,String xmlDoc) {
		try {
			Writer writer = new FileWriter(artifactFile);
			writer.write(xmlDoc);
			writer.close();
		} catch (IOException e) {
			throw new SonarException(e);
		}
	}
	


	private File createModuleDir(File artifactFile) {

		File moduleDir=artifactFile.getParentFile();
		if(!moduleDir.exists()) {
			if(!moduleDir.mkdirs()) {
				throw new SonarException("Could not create dir " + moduleDir.getAbsolutePath());
			}
		}
		if(!moduleDir.isDirectory()) {
			throw new SonarException("Is not a directory " + moduleDir.getAbsolutePath());
		}
		return moduleDir;
	}
	
	/**
	 * A project wants to have its coverage file. It has an artifact associated to it. The coverage files are stored
	 * as  <root>/<artifact>/<projectName>.xml
	 * @param artifactName
	 * @return
	 */
	@Override
	public File getArtifactCoverageFile(String artifactName) {
		Preconditions.checkNotNull(projectName);
		String relativePath=removeSuffix(artifactName)+ "/" + projectName + ".xml";
		return new File(root,relativePath);
	}
	
	private final  Pattern pattern = Pattern.compile("(.*)\\.(dll|exe)$");
	
	private String removeSuffix(String moduleName) {
		Matcher matcher = pattern.matcher(moduleName);
		String module=matcher.find()?matcher.group(1):moduleName;
		return module;
	}

}
