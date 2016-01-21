package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;

public interface ModuleLambda {

	/**
	 * 
	 * @param sb fully qualified xml document
	 */
	void execute(String sb);

	/**
	 * set the name of the project being analysed, will determine the name of the created xml files
	 * @param projectName
	 * @return
	 */
	ModuleLambda setProject(String projectName);

	/**
	 * set directory below which the coverage directories will be created
	 * @param root
	 * @return
	 */
	ModuleLambda setDirectory(File root);

	File getArtifactCoverageFile(String artifactName);

}
