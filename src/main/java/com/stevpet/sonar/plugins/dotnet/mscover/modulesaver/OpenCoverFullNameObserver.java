package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.parser.observer.StartObserverRegistrar;


public class OpenCoverFullNameObserver  extends ModuleFullNameObserver {
    private String moduleName ;
    private boolean skipped;

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.ModuleFullNameObserver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return moduleName;
	}

	@Override
	public void registerObservers(StartObserverRegistrar registrar) {
	    registrar.inPath("Modules")
	        .onEntry("Module",() -> skipped=false)
	        .inElement("Module").onAttribute("skippedDueTo",this::observeSkippedDueAttribute);
	    registrar.inPath("Modules/Module").onElement("ModuleName",this::setModuleName);
	}

    public void setModuleName(String value) {
    	int lastDirSep = value.lastIndexOf("\\");
    	moduleName=value.substring(lastDirSep+1);	
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
