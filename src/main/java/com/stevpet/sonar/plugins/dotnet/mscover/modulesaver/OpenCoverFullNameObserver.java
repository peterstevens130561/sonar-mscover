package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverPaths;


public class OpenCoverFullNameObserver  extends ModuleFullNameObserver {

	private static final String MODULES_MODULE = "Modules/Module";

    private String moduleName ;
    private boolean skipped;
	
	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.ModuleFullNameObserver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return moduleName;
	}
	public OpenCoverFullNameObserver() {
		setPattern(OpenCoverPaths.MODULE_FULLPATH + "|" + MODULES_MODULE);
	}

    @PathMatcher(path=OpenCoverPaths.MODULE_FULLPATH)
    public void setModuleName(String value) {
    	int lastDirSep = value.lastIndexOf("\\");
    	moduleName=value.substring(lastDirSep+1);	
    }
    
    @AttributeMatcher(attributeName="skippedDueTo", elementName = "Module")
    public void observeSkippedDueAttribute(String value) {
        if(StringUtils.isNotEmpty(value)) {
            skipped=true;
        }
    }
    

    @Override
    public boolean getSkipped() {
        return skipped;
    }
}
