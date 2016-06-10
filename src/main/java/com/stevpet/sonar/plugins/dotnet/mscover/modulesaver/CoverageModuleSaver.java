package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import java.io.File;



interface CoverageModuleSaver  {
    /**
     * set the name of the project being analysed, will determine the name of the created xml files
     * @param projectName
     * @return
     */
    CoverageModuleSaver setProject(String projectName);

    /**
     * set directory below which the coverage directories will be created
     * @param root
     * @return
     */
    CoverageModuleSaver setDirectory(File root);
	/**
	 * 
	 * @param sb fully qualified xml document
	 */
	void save(String sb);

}
