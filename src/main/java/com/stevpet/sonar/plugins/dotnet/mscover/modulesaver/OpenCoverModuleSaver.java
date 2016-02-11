package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;

import javax.annotation.Nonnull;

/**
 * 
 * @author stevpet
 *
 */
public class OpenCoverModuleSaver extends OpenCoverModuleSplitterBase {

	private static ModuleSaverLambda moduleSaverLambda=new ModuleSaverLambda( new OpenCoverModuleParser() );
	public OpenCoverModuleSaver() {
		super(moduleSaverLambda );
	}

	public OpenCoverModuleSaver setRoot(File root) {
		moduleSaverLambda.setDirectory(root);
		return this;
	}
	public OpenCoverModuleSaver setProject(@Nonnull String projectName) {
		moduleSaverLambda.setProject(projectName);
		return this;
	}

	/**
	 * gets the coverage file of the current artifact
	 * @return
	 */
	public File getCoverageFile(String artifact) {
		return moduleSaverLambda.getArtifactCoverageFile(artifact);
	}
	
}
