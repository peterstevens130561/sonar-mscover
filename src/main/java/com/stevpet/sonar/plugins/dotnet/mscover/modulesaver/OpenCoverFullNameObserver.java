package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.api.parser.annotations.AttributeMatcher;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver;
import com.stevpet.sonar.plugins.common.api.parser.annotations.ElementObserver.Event;
import com.stevpet.sonar.plugins.common.api.parser.annotations.PathMatcher;
import com.stevpet.sonar.plugins.common.parser.ObserverRegistrar;
import com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.OpenCoverPaths;


public class OpenCoverFullNameObserver  extends ModuleFullNameObserver {

	private static final String MODULES_MODULE = "Modules/Module";

    private String moduleName ;
    private boolean skipped;

    public OpenCoverFullNameObserver() {
        setPattern(OpenCoverPaths.MODULE_FULLPATH + "|" + MODULES_MODULE);
    }

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.ModuleFullNameObserver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return moduleName;
	}

	@Override
	public void registerObservers(ObserverRegistrar registrar) {
	    registrar.onPath(this::setModuleName, OpenCoverPaths.MODULE_FULLPATH)
	    .onAttribute("Module/skippedDueTo",this::observeSkippedDueAttribute);    
	}

    public void setModuleName(String value) {
    	int lastDirSep = value.lastIndexOf("\\");
    	moduleName=value.substring(lastDirSep+1);	
    }
    
   
    @ElementObserver(  path=OpenCoverPaths.MODULE, event = Event.ENTRY ) 
    public void observeModule() {
        skipped=false;
    }
    
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
