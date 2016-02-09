package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;


public class OpenCoverFullNameObserver  extends ModuleFullNameObserver {

	private String moduleName ;
	
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.ModuleFullNameObserver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return moduleName;
	}
	public OpenCoverFullNameObserver() {
		setPattern("Modules/Module/FullName");
	}

    @PathMatcher(path="Modules/Module/FullName")
    public void setModuleName(String value) {
    	int lastDirSep = value.lastIndexOf("\\");
    	moduleName=value.substring(lastDirSep+1);	
    }
}
