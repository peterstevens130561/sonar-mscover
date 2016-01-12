package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;

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

	public void setRoot(File root) {
		moduleSaverLambda.setDirectory(root);
	}
	
	
}
